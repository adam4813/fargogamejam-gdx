package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents the storage capacity of a ship. */
class CargoBay implements PartModules{
	/** Represents the resource's capacity, [10..100]. */
	private final int maxFuel;
	/** Represents the resource's capacity, [10..100]. */
	private final int maxMetal;
	/** Represents the resource's capacity, [10..100]. */
	private final int maxWater;
	/** Represents the resource's capacity, [10..100]. */
	private final int maxGas;
	/** Represents the resource's capacity, [10..100]. */
	private final int maxAmmo;
	/** Represents the resource's capacity, [10..100]. */
	private final int maxFood;
	/** Represents the ship's unused modules. */
	private final List<PartModules> parts;
	/** Represents the resource's current quantity, [0..maxFuel]. */
	private int fuel;
	/** Represents the resource's current quantity, [0..maxMetal]. */
	private int metal;
	/** Represents the resource's current quantity, [0..maxWater]. */
	private int water;
	/** Represents the resource's current quantity, [0..maxGas].
	 * Sustains crew members of compatible atmospheric compositions. */
	private int gas;
	/** Represents the resource's current quantity, [0..maxAmmo]. */
	private int ammo;
	/** Represents the resource's current quantity, [0..maxFood].
	 * Removes damage from crew members. */
	private int food;
	/** Represents whether the artifact has been found or not. */
	private boolean hasArtifact;
	/** Instantiates a Cargo Bay with the provided parameters, unless they are out of range. If a parameter is out of range it defaults to minimal defaults.
	 * @param maxAmmo {@link CargoBay#maxAmmo}
	 * @param maxFood {@link CargoBay#maxFood}
	 * @param maxFuel {@link CargoBay#maxFuel}
	 * @param maxGas {@link CargoBay#maxGas}
	 * @param maxMetal {@link CargoBay#maxMetal}
	 * @param maxWater {@link CargoBay#maxWater}
	 * @param ammo {@link CargoBay#ammo}
	 * @param food {@link CargoBay#food}
	 * @param fuel {@link CargoBay#fuel}
	 * @param gas {@link CargoBay#gas}
	 * @param metal {@link CargoBay#metal}
	 * @param water {@link CargoBay#water}
	 * @param parts {@link CargoBay#parts}
	 */
	CargoBay(boolean hasArtifact, int maxAmmo, int maxFood, int maxFuel, int maxGas, int maxMetal, int maxWater, int ammo, int food, int fuel, int gas, int metal, int water, List<PartModules> parts) {
		this.hasArtifact = hasArtifact;
		this.maxAmmo = Galaxy.initializeWithConstraints(maxAmmo, 10, 100, 10);
		this.ammo = Galaxy.initializeWithConstraints(ammo, 0, this.maxAmmo, 0);
		this.maxFood = Galaxy.initializeWithConstraints(maxFood, 10, 100, 10);
		this.food = Galaxy.initializeWithConstraints(food, 0, this.maxFood, 0);
		this.maxFuel = Galaxy.initializeWithConstraints(maxFuel, 10, 100, 10);
		this.fuel = Galaxy.initializeWithConstraints(fuel, 0, this.maxFuel, 0);
		this.maxGas = Galaxy.initializeWithConstraints(maxGas, 10, 100, 10);
		this.gas = Galaxy.initializeWithConstraints(gas, 0, this.maxGas, 0);
		this.maxMetal = Galaxy.initializeWithConstraints(maxMetal, 10, 100, 10);
		this.metal = Galaxy.initializeWithConstraints(metal, 0, this.maxMetal, 0);
		this.maxWater = Galaxy.initializeWithConstraints(maxWater, 10, 100, 10);
		this.water = Galaxy.initializeWithConstraints(water, 0, this.maxWater, 0);
		if (parts == null) {
			this.parts = new ArrayList<>();
		} else {
			this.parts = parts;
		}
	}

	/** Attempts to store gathered resources.
	 * @param stored represents the resources already in the cargo bay.
	 * @param gathered represents the resources to add to the cargo bay. Cannot be negative, will be set to 0.
	 * @param max represents the most the cargo bay can hold.
	 * @return an int, quantity, that represents the new resource quantity in the cargo bay.
	 * @throws CargoException if the amount added would bring the quantity beyond the max.
	 */
	private int store(int stored, int gathered, int max) throws CargoException{
		if(gathered < 0){
			gathered = 0;
		}
		int quantity = stored + gathered;
		if (quantity > max) {
			throw new CargoException(CargoException.Problems.OVERFLOW, stored + gathered - max);
		}
		return quantity;
	}

	/** Attempts to deplete taken resources.
	 * @param stored represents the resources already in the cargo bay.
	 * @param taken represents the resources to remove from the cargo bay. Cannot be negative, will be set to 0.
	 * @return an int, quantity, that represents the new resource quantity in the cargo bay.
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
	 * @param resource represents the type of resource being stored.
	 * @param quantity represents the amount of the resource being stored. Ignored in the case of {@link ResourceTypes#ARTIFACT}.
	 * @return an int representing the amount of overflow. -1 if an unknown resource type was used.
	 * @see CargoBay#store(int, int, int)
	 * @see CargoBay#storePart(PartModules)
	 */
	int storeResource(ResourceTypes resource, int quantity){
		int overflow = 0;
		switch(resource){
			case ARTIFACT:
				if(!this.hasArtifact){
					this.hasArtifact = true;
				}else{
					overflow = 1;
				}
				break;
			case AMMO:
				try{
					this.ammo = store(this.ammo, quantity, this.maxAmmo);
				}catch(CargoException caught){
					try{
						this.ammo = store(this.ammo, quantity - caught.getQuantity(), this.maxAmmo);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case FOOD:
				try{
					this.food = store(this.food, quantity, this.maxFood);
				}catch(CargoException caught){
					try{
						this.food = store(this.food, quantity - caught.getQuantity(), this.maxFood);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case FUEL:
				try{
					this.fuel = store(this.fuel, quantity, this.maxFuel);
				}catch(CargoException caught){
					try{
						this.fuel = store(this.fuel, quantity - caught.getQuantity(), this.maxFuel);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case GAS:
				try{
					this.gas = store(this.gas, quantity, this.maxGas);
				}catch(CargoException caught){
					try{
						this.gas = store(this.gas, quantity - caught.getQuantity(), this.maxGas);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case METAL:
				try{
					this.metal = store(this.metal, quantity, this.maxMetal);
				}catch(CargoException caught){
					try{
						this.metal = store(this.metal, quantity - caught.getQuantity(), this.maxMetal);
						overflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot overflow. */
					}
				}
				break;
			case WATER:
				try{
					this.water = store(this.water, quantity, this.maxWater);
				}catch(CargoException caught){
					try{
						this.water = store(this.water, quantity - caught.getQuantity(), this.maxWater);
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

	/**
	 * @param resource represents the type of resource being depleted.
	 * @param quantity represents the amount of the resource being depleted. Ignored in the case of {@link ResourceTypes#ARTIFACT}.
	 * @return an int representing the amount of underflow. -1 if an unknown resource type was used.
	 * @see CargoBay#deplete(int, int)
	 * @see CargoBay#removepart(PartModules)
	 */
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
			case AMMO:
				try{
					this.ammo = deplete(this.ammo, quantity);
				}catch(CargoException caught){
					try{
						this.ammo = deplete(this.ammo, quantity - caught.getQuantity());
						underflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot underflow. */
					}
				}
				break;
			case FOOD:
				try{
					this.food = deplete(this.food, quantity);
				}catch(CargoException caught){
					try{
						this.food = deplete(this.food, quantity - caught.getQuantity());
						underflow = caught.getQuantity();
					}catch(CargoException notCaught){
						/* The value has been adjusted and cannot underflow. */
					}
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
			case GAS:
				try{
					this.gas = deplete(this.gas, quantity);
				}catch(CargoException caught){
					try{
						this.gas = deplete(this.gas, quantity - caught.getQuantity());
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
	 * @param resource represents the resource of interest.
	 * @return an int representing the quantity of the given resource in the cargo bay. Returns -1 if the resource type isn't recognized.
	 * @see CargoBay#getStoredParts()
	 */
	int checkResource(ResourceTypes resource){
		int quantity;
		switch(resource){
			case AMMO:
				quantity = this.ammo;
				break;
			case ARTIFACT:
				quantity = this.hasArtifact ? 1 : 0;
				break;
			case FOOD:
				quantity = this.food;
				break;
			case FUEL:
				quantity = this.fuel;
				break;
			case GAS:
				quantity = this.gas;
				break;
			case METAL:
				quantity = this.metal;
				break;
			case WATER:
				quantity = this.water;
				break;
			default:
				quantity = -1;
				break;
		}
		return quantity;
	}

	/**
	 * @param part represents the ship module to store in the cargo bay. Cannot be null.
	 */
	void storePart(PartModules part) {
		if (part == null) {
			throw new IllegalArgumentException("Must include a part to be added");
		}
		this.parts.add(part);
	}

	/**
	 * @param part represents the ship module to remove from the cargo bay. Cannot be null.
	 */
	void removepart(PartModules part) {
		if (part == null) {
			throw new IllegalArgumentException("Must reference a part to be removed");
		}
		this.parts.remove(part);
	}

	/**
	 * Ensures the artifact, resources, and parts are moved to the other cargo bay.
	 * @param other represents the cargo bay to receive the cargo.
	 */
	void transferCargoTo(CargoBay other){
		/* Transfer the artifact. */
		if(this.hasArtifact){
			other.storeResource(ResourceTypes.ARTIFACT, 1);
		}
		/* Store the resources in other and deplete them from this. */
		int quantity;
		for(ResourceTypes rt : ResourceTypes.values()){
			if(!rt.equals(ResourceTypes.ARTIFACT)){
				quantity = this.checkResource(rt);
				other.storeResource(rt, quantity);
				this.depleteResource(rt, quantity);
			}
		}
		/* Store parts. */
		for(PartModules pm : this.getStoredParts()){
			other.storePart(pm);
		}
		/* Ensure the parts don't remain duplicated. */
		this.parts.clear();
	}

	/**
	 * @return an unmodifiable list of the parts stored.
	 */
	List<PartModules> getStoredParts() {
		return Collections.unmodifiableList(this.parts);
	}

	@Override
	public int getModuleLevel() {
		int level = (this.maxAmmo + this.maxFood + this.maxFuel + this.maxGas + this.maxMetal + this.maxWater) / 125;
		return Math.min(level, 4);
	}
}
