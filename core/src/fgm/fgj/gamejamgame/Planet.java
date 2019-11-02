package fgm.fgj.gamejamgame;
import java.util.ArrayList;
import java.util.List;

/** Represents a celestial body with resources and potentially habitable by crew to end the game. */
public class Planet {
	/** Cannot be null.
	 * @see PlanetType
	 */
	final PlanetType planetType;
	/** Represents the identifier for the planet, cannot be null or "". */
	private final String name;
	/** Cannot be null.
	 * @see AtmosphericCompositions
	 */
	private final AtmosphericCompositions airType;
	/** Represents the inhabitants of the planet, cannot be null. Each species should be compatible with the planet. */
	private final List<Species> speciesPresent;
	/** Represents the gravity strength of the planet, [0..5]. */
	private final int gravity;
	/** Represents the atmospheric pressure of the planet, [0..5]. */
	private final int atmosphericPressure;
	/** Represents the temperature of the planet, [0..5]. */
	private final int temperature;
	/** Represents the amount of exploitable resources on the planet, at least 0. */
	private int fuel;
	/** Represents the amount of exploitable resources on the planet, at least 0. */
	private int metal;
	/** Represents the amount of exploitable resources on the planet, at least 0. */
	private int water;
	/** Represents, when true, that the artifact can be found on the planet. */
	private boolean hasArtifact;

	/**
	 * Instantiates a new planet based on parameters given if they are within range, otherwise midpoint defaults are used.
	 * @param name {@link Planet#name}
	 * @param airType {@link Planet#airType}
	 * @param speciesPresent {@link Planet#speciesPresent} any species not compatible are removed from the list.
	 * @param gravity {@link Planet#gravity}
	 * @param atmosphericPressure {@link Planet#atmosphericPressure}
	 * @param temperature {@link Planet#temperature}
	 * @param fuel {@link Planet#fuel}
	 * @param metal {@link Planet#metal}
	 * @param water {@link Planet#water}
	 * @param hasArtifact {@link Planet#hasArtifact}
	 */
	Planet(String name, AtmosphericCompositions airType, List<Species> speciesPresent, int gravity, int atmosphericPressure, int temperature, int fuel, int metal, int water, boolean hasArtifact) {
		if(name == null){
			throw new IllegalArgumentException("A planet must have a non null name.");
		}
		if(name.equals("")){
			throw new IllegalArgumentException("A planet must have a name.");
		}
		if(airType == null){
			throw new IllegalArgumentException("A planet must have a atmospheric composition.");
		}
		if(speciesPresent == null){
			/* Assume they meant to have no species instead of creating an invalid planet. */
			speciesPresent = new ArrayList<>();
		}
		this.planetType = PlanetType.getRandomPlanetType();
		this.name = name;
		this.airType = airType;
		this.speciesPresent = speciesPresent;
		this.gravity = Galaxy.initializeWithConstraints(gravity, 0, 5, 2);
		this.atmosphericPressure = Galaxy.initializeWithConstraints(atmosphericPressure, 0, 5, 2);
		this.temperature = Galaxy.initializeWithConstraints(temperature, 0, 5, 2);
		this.fuel = Galaxy.initializeWithConstraints(fuel, 0, 250, 0);
		this.metal = Galaxy.initializeWithConstraints(metal, 0, 250, 0);
		this.water = Galaxy.initializeWithConstraints(water, 0, 250, 0);
		this.hasArtifact = hasArtifact;
		/* Remove any species that could not survive on the planet. */
		for(int i = speciesPresent.size() - 1; i >= 0; i--){
			if(!this.isHabitablePlanet(speciesPresent.get(i))){
				this.speciesPresent.remove(i);
			}
		}
	}

	/**
	 * @see Planet#name
	 */
	public String getName() {
		return name;
	}

	/** Attempts to store replenished resources.
	 * @param stored represents the resources already on the planet.
	 * @param replenished represents the resources to add to the planet. Cannot be negative, will be set to 0.
	 * @param max represents the most the planet can hold.
	 * @return an int, quantity, that represents the new resource quantity on the planet.
	 * @throws CargoException if the amount added would bring the quantity beyond the max.
	 */
	private int store(int stored, int replenished, int max) throws CargoException{
		if(replenished < 0){
			replenished = 0;
		}
		int quantity = stored + replenished;
		if (quantity > max) {
			throw new CargoException(CargoException.Problems.OVERFLOW, stored + replenished - max);
		}
		return quantity;
	}

	/** Attempts to deplete taken resources.
	 * @param stored represents the resources already in the planet.
	 * @param taken represents the resources to remove from the planet.
	 * @return an int, quantity, that represents the new resource quantity in the planet.
	 * @throws CargoException if the amount removed would bring the quantity below zero.
	 */
	private int deplete(int stored, int taken) throws CargoException{
		if(taken < 0){
			taken = 0;
		}
		int quantity = stored - taken;
		if (quantity < 0) {
			throw new CargoException(CargoException.Problems.UNDERFLOW, taken - stored);
		}
		return quantity;
	}

	/**
	 * @param resource represents the type of resource being replenished.
	 * @param quantity represents the amount of the resource being replenished. Ignored in the case of {@link ResourceTypes#ARTIFACT}.
	 * @return an int representing the amount of overflow. -1 if an unknown resource type was used.
	 * @see Planet#store(int, int, int)
	 */
	int replenishResource(ResourceTypes resource, int quantity){
		int overflow = 0;
		switch(resource){
			case ARTIFACT:
				if(!this.hasArtifact){
					this.hasArtifact = true;
				}else{
					overflow = 1;
				}
				break;
			case FUEL:
				try{
					this.fuel = store(this.fuel, quantity, 1000);
				}catch(CargoException caught){
					try{
						this.fuel = store(this.fuel, quantity - caught.getQuantity(), 1000);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case METAL:
				try{
					this.metal = store(this.metal, quantity, 1000);
				}catch(CargoException caught){
					try{
						this.metal = store(this.metal, quantity - caught.getQuantity(), 1000);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case WATER:
				try{
					this.water = store(this.water, quantity, 1000);
				}catch(CargoException caught){
					try{
						this.water = store(this.water, quantity - caught.getQuantity(), 1000);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			default:
				overflow = -1;
				break;
		}
		return overflow;
	}

	/** @see Planet#deplete(int, int) */
	int depleteResource(ResourceTypes resource, int quantity){
		int underflow = 0;
		switch(resource){
			case ARTIFACT:
				if(this.hasArtifact){
					this.hasArtifact = false;
				}else{
					underflow = 1;
				}
				break;
			case FUEL:
				try{
					this.fuel = deplete(this.fuel, quantity);
				}catch(CargoException caught){
					try{
						this.fuel = deplete(this.fuel, quantity - caught.getQuantity());
						underflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot underflow. */
					}
				}
				break;
			case METAL:
				try{
					this.metal = deplete(this.metal, quantity);
				}catch(CargoException caught){
					try{
						this.metal = deplete(this.metal, quantity - caught.getQuantity());
						underflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot underflow. */
					}
				}
				break;
			case WATER:
				try{
					this.water = deplete(this.water, quantity);
				}catch(CargoException caught){
					try{
						this.water = deplete(this.water, quantity - caught.getQuantity());
						underflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot underflow. */
					}
				}
				break;
			default:
				underflow = -1;
				break;
		}
		return underflow;
	}

	/**
	 * @param resource should use ResourceTypes#ARTIFACT, ResourceTypes#FUEL, ResourceTypes#METAL, ResourceTypes#WATER.
	 * @return an int, quantity, that represents the amount of the provided resource is exploitable on the planet. If a resource doesn't match one available on planets -1 is returned.
	 */
	int checkResource(ResourceTypes resource){
		int quantity;
		switch(resource){
			case ARTIFACT:
				quantity = this.hasArtifact ? 1 : 0;
				break;
			case FUEL:
				quantity = this.fuel;
				break;
			case METAL:
				quantity = this.metal;
				break;
			case WATER:
				quantity = this.water;
				break;
			default:
				quantity = -1;
		}
		return quantity;
	}

	/**
	 * @return a species from {@link Planet#speciesPresent} with the most damage.
	 */
	Species getMostViolentSpecies() {
		Species mostViolentSpecies = null;
		for (Species species : this.speciesPresent) {
			if (species.damage > ((mostViolentSpecies == null) ? 0 : mostViolentSpecies.damage)) {
				mostViolentSpecies = species;
			}
		}
		return mostViolentSpecies;
	}

	/** Compares the provided ship's crew with the planet to see if the species of the crew are compatible with the planet's attributes.
	 * @param ship represents the player and must have a crew.
	 * @return a boolean that represents, when true, that the crew can inhabit the planet.
	 */
	public boolean isHabitablePlanet(Ship ship) {
		boolean isHabitable = false;
		if(ship != null){
			isHabitable = true;
			for(CrewMember cm : ship.getCrewMembers()){
				isHabitable &= this.isHabitablePlanet(cm.species);
				if(!isHabitable){
					break;
				}
			}
		}


		return isHabitable;
	}

	/** Checks whether the species could survive on the planet.
	 * @param species represents the species to check if it is compatible with the planet.
	 * @return a boolean that represents, when true, that the species is compatible with the planet.
	 */
	private boolean isHabitablePlanet(Species species){
		boolean isHabitable = false;
		if(species != null){
			isHabitable = true;
			isHabitable &= this.gravity == species.gravityTolerance;
			isHabitable &= this.atmosphericPressure == species.atmosphericPressureTolerance;
			isHabitable &= this.temperature == species.temperatureTolerance;
			isHabitable &= this.airType == species.atmosphericCompositionTolerance;
		}
		return isHabitable;
	}
}
