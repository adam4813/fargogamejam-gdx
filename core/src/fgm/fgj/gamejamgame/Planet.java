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
	/** Represents the inhabitants of the planet, cannot be null. */
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
	 * @param speciesPresent {@link Planet#speciesPresent}
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
		this.fuel = Galaxy.initializeWithConstraints(fuel, 0, 125, 0);
		this.metal = Galaxy.initializeWithConstraints(metal, 0, 125, 0);
		this.water = Galaxy.initializeWithConstraints(water, 0, 125, 0);
		this.hasArtifact = hasArtifact;
	}

	/**
	 * @see Planet#name
	 */
	public String getName() {
		return name;
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

	/** @see Planet#deplete(int, int) */
	void depleteResource(ResourceTypes resource, int quantity) throws CargoException{
		switch(resource){
			case ARTIFACT:
				if(this.hasArtifact){
					this.hasArtifact = false;
				}else{
					throw new CargoException(CargoException.Problems.UNDERFLOW, -1);
				}
				break;
			case FUEL:
				this.fuel = deplete(this.fuel, quantity);
				break;
			case METAL:
				this.metal = deplete(this.metal, quantity);
				break;
			case WATER:
				this.water = deplete(this.water, quantity);
				break;
			default:
				throw new CargoException(CargoException.Problems.UNKNOWN_RESOURCE, -1);
		}
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
			if (mostViolentSpecies == null || species.damage > mostViolentSpecies.damage) {
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
		if(ship == null){
			throw new IllegalArgumentException("A planet cannot be habitable with a null ship.");
		}
		boolean isHabitable = true;
		List<CrewMember> crewMembers = ship.getCrewMembers();
		for(CrewMember cm : crewMembers){
			Species s = cm.species;
			isHabitable &= this.gravity == s.gravityTolerance;
			isHabitable &= this.atmosphericPressure == s.atmosphericPressureTolerance;
			isHabitable &= this.temperature == s.temperatureTolerance;
			isHabitable &= this.airType == s.atmosphericCompositionTolerance;
			if(!isHabitable){
				break;
			}
		}
		return isHabitable;
	}
}
