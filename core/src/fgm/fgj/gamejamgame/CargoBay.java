package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

public class CargoBay {
	private int fuel;
	private int maxFuel;
	private int metal;
	private int maxMetal;
	private int water;
	private int maxWater;
	private int gas;
	private int maxGas;
	private List<PartModules> parts;
	private int ammo;
	private int maxAmmo;
	private int food;
	private int maxFood;

	/**
	 * @param value desired value
	 * @param min lowest allowed value
	 * @param max highest allowed value
	 * @param def default value if desired value is out of range
	 * @return value if valid, otherwise default
	 */
	private int withinRangeOrDefault(int value, int min, int max, int def) {
		if (value >= min && value <= max) {
			return value;
		} else {
			return def;
		}
	}

	/**
	 * @param maxFuel int (25-100) indicating maximum fuel storage capacity - default 50
	 * @param maxMetal int (5-25) indicating maximum metal storage capacity - default 15
	 * @param maxWater int (5-50) indicating maximum water storage capacity - default 25
	 * @param maxGas int (5-50) indicating maximum gas storage capacity - default 25
	 * @param maxAmmo int (50-200) indicating maximum ammo storage capacity - default 100
	 * @param maxFood int (25-100) indicating maximum food storage capacity - default 50
	 */
	public CargoBay (int maxFuel, int maxMetal, int maxWater, int maxGas, int maxAmmo, int maxFood) {
		this.maxFuel = this.withinRangeOrDefault(maxFuel, 25, 100, 50);
		this.fuel = this.maxFuel; // Assuming you would have fueled up before starting out
		this.maxMetal = this.withinRangeOrDefault(maxMetal, 5, 25, 15);
		this.metal = 0;
		this.maxWater = this.withinRangeOrDefault(maxWater, 5, 50, 25);
		this.water = this.maxWater; // Assuming you topped off water before starting out
		this.maxGas = this.withinRangeOrDefault(maxGas, 5, 50, 25);
		this.gas = 0;
		this.parts = new ArrayList<>();
		this.maxAmmo = this.withinRangeOrDefault(maxAmmo, 50, 200, 100);
		this.ammo = this.maxAmmo; // Assuming stocked armory before starting out
		this.maxFood = this.withinRangeOrDefault(maxFood, 25, 100, 50);
		this.food = this.maxFood; // Assuming you packed your lunch
	}

	/**
	 * @param current current level
	 * @param add amount to add
	 * @param max maximum capacity
	 * @return new level
	 */
	private int addWithinRange(int current, int add, int max) {
		if (current + add > max) {
			return max;
		} else {
			return current + add;
		}
	}

	/**
	 * @param current current level
	 * @param remove amount to remove
	 * @return new level
	 */
	private int decreaseUntilZero(int current, int remove) {
		if (current - remove < 0) {
			return 0;
		} else {
			return current - remove;
		}
	}

	public void increaseFuel(int newFuel) {
		this.fuel = this.addWithinRange(this.fuel, newFuel, this.maxFuel);
	}
	public void decreaseFuel(int usedFuel) {
		this.fuel = this.decreaseUntilZero(this.fuel, usedFuel);
	}
	public int checkFuel() {
		return this.fuel;
	}

	public void increaseMetal(int newMetal) {
		this.metal = this.addWithinRange(this.metal, newMetal, this.maxMetal);
	}
	public void decreaseMetal(int usedMetal) {
		this.metal = this.decreaseUntilZero(this.metal, usedMetal);
	}
	public int checkMetal() {
		return this.metal;
	}

	public void increaseWater(int newWater) {
		this.water = this.addWithinRange(this.water, newWater, this.maxWater);
	}
	public void decreaseWater(int usedWater) {
		this.water = this.decreaseUntilZero(this.water, usedWater);
	}
	public int checkWater() {
		return this.water;
	}

	public void increaseGas(int newGas) {
		this.gas = this.addWithinRange(this.gas, newGas, this.maxGas);
	}
	public void decreaseGas(int usedGas) {
		this.gas = this.decreaseUntilZero(this.gas, usedGas);
	}
	public int checkGas() {
		return this.gas;
	}

	public void increaseAmmo(int newAmmo) {
		this.ammo = this.addWithinRange(this.ammo, newAmmo, this.maxAmmo);
	}
	public void decreaseAmmo(int usedAmmo) {
		this.ammo = this.decreaseUntilZero(this.ammo, usedAmmo);
	}
	public int checkAmmo() {
		return this.ammo;
	}

	public void increaseFood(int newFood) {
		this.food = this.addWithinRange(this.food, newFood, this.maxFood);
	}
	public void decreaseFood(int usedFood) {
		this.food = this.decreaseUntilZero(this.food, usedFood);
	}
	public int checkFood() {
		return this.food;
	}

	public void addPart(PartModules part) {
		this.parts.add(part);
	}
	public void removePart() {
		// TODO: what do we pass in to select a part to remove?
	}
	public List<PartModules> listParts() {
		return this.parts;
	}
}
