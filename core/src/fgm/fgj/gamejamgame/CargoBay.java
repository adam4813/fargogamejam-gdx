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
	/** Represents the resource's current quantity, [0..maxGas]. */
	private int gas;
	/** Represents the resource's current quantity, [0..maxAmmo]. */
	private int ammo;
	/** Represents the resource's current quantity, [0..maxFood]. */
	private int food;
	/** Represents whether the artifact has been found or not. */
	private boolean hasArtifact;
	/** Instantiates a Cargo Bay with the provided parameters, unless they are out of range. If a parameter is out of range it defaults to a maximum of 25 and a 0 quantity.
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
		this.maxAmmo = PartModules.initializeWithConstraints(maxAmmo, 10, 100, 25);
		this.ammo = PartModules.initializeWithConstraints(ammo, 0, this.maxAmmo, 0);
		this.maxFood = PartModules.initializeWithConstraints(maxFood, 10, 100, 25);
		this.food = PartModules.initializeWithConstraints(food, 0, this.maxFood, 0);
		this.maxFuel = PartModules.initializeWithConstraints(maxFuel, 10, 100, 25);
		this.fuel = PartModules.initializeWithConstraints(fuel, 0, this.maxFuel, 0);
		this.maxGas = PartModules.initializeWithConstraints(maxGas, 10, 100, 25);
		this.gas = PartModules.initializeWithConstraints(gas, 0, this.maxGas, 0);
		this.maxMetal = PartModules.initializeWithConstraints(maxMetal, 10, 100, 25);
		this.metal = PartModules.initializeWithConstraints(metal, 0, this.maxMetal, 0);
		this.maxWater = PartModules.initializeWithConstraints(maxWater, 10, 100, 25);
		this.water = PartModules.initializeWithConstraints(water, 0, this.maxWater, 0);
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
	 * @throws CargoException if capacity for the resource is exceeded or an unknown resource is used.
	 * @see CargoBay#store(int, int, int)
	 * @see CargoBay#storePart(PartModules)
	 */
	void storeResource(ResourceTypes resource, int quantity) throws CargoException{
		switch(resource){
			case ARTIFACT:
				this.hasArtifact = true;
				break;
			case AMMO:
				this.ammo = store(this.ammo, quantity, this.maxAmmo);
				break;
			case FOOD:
				this.food = store(this.food, quantity, this.maxFood);
				break;
			case FUEL:
				this.fuel = store(this.fuel, quantity, this.maxFuel);
				break;
			case GAS:
				this.gas = store(this.gas, quantity, this.maxGas);
				break;
			case METAL:
				this.metal = store(this.metal, quantity, this.maxMetal);
				break;
			case WATER:
				this.water = store(this.water, quantity, this.maxWater);
				break;
			default:
				throw new CargoException(CargoException.Problems.UNKNOWN_RESOURCE, -1);
		}
	}

	/**
	 * @param resource represents the type of resource being depleted.
	 * @param quantity represents the amount of the resource being depleted. Ignored in the case of {@link ResourceTypes#ARTIFACT}.
	 * @throws CargoException if the resource quantity is exceeded or an unknown resource is used.
	 * @see CargoBay#deplete(int, int)
	 * @see CargoBay#removepart(PartModules)
	 */
	void depleteResource(ResourceTypes resource, int quantity) throws CargoException{
		switch(resource){
			case ARTIFACT:
				this.hasArtifact = false;
				break;
			case AMMO:
				this.ammo = deplete(this.ammo, quantity);
				break;
			case FOOD:
				this.food = deplete(this.food, quantity);
				break;
			case FUEL:
				this.fuel = deplete(this.fuel, quantity);
				break;
			case GAS:
				this.gas = deplete(this.gas, quantity);
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
			try{
				other.storeResource(ResourceTypes.ARTIFACT, 1);
			}catch(CargoException caught){
				/* Cannot happen since the enum is used and there is no quantity to overflow with as it is ignored. */
			}
		}
		/* Store the resources in other and deplete them from this. */
		int quantity;
		for(ResourceTypes rt : ResourceTypes.values()){
			if(!rt.equals(ResourceTypes.ARTIFACT)){
				quantity = this.checkResource(rt);
				try{
					other.storeResource(rt, quantity);
				}catch(CargoException caught){
					/* Some resources are lost due to the transferred cargo bay being smaller in this capacity. */
					try{
						other.storeResource(rt, quantity - caught.getQuantity());
					}catch(CargoException caught2){
						/* Cannot happen since the quantity was adjusted by the overflow amount. */
					}
				}
				try{
					/* Ensure the resources don't remain duplicated. */
					this.depleteResource(rt, quantity);
				}catch(CargoException caught){
					/* Cannot happen since the quantities are checked before. */
				}
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

	@SuppressWarnings("javadocs")
	@Override
	public int getModuleLevel() {
		int level = (this.maxAmmo + this.maxFood + this.maxFuel + this.maxGas + this.maxMetal + this.maxWater) / 125;
		return Math.min(level, 4);
	}
}
