package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CargoBay {
	private final int maxFuel;
	private final int maxMetal;
	private final int maxWater;
	private final int maxGas;
	private final int maxAmmo;
	private final int maxFood;
	private int fuel;
	private int metal;
	private int water;
	private int gas;
	private int ammo;
	private int food;
	private final List<PartModules> parts;

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
	 *
	 * @param fuel int (0-maxFuel) indicating current fuel level - default maxFuel
	 * @param metal int (0-maxMetal) indicating current metal level - default maxMetal
	 * @param water int (0-maxWater) indicating current water level - default maxWater
	 * @param gas int (0-maxGas) indicating current gas level - default maxGas
	 * @param ammo int (0-maxAmmo) indicating current ammo level - default maxAmmo
	 * @param food int (0-maxFood) indicating current food level - default maxFood
	 *
	 * @param parts list of parts present in the cargo bay
	 */
	public CargoBay (
		int maxFuel, int maxMetal, int maxWater, int maxGas, int maxAmmo, int maxFood,
		int fuel, int metal, int water, int gas, int ammo, int food,
		List<PartModules> parts
	) {
		this.maxFuel = this.withinRangeOrDefault(maxFuel, 10, 100, 30);
		this.maxMetal = this.withinRangeOrDefault(maxMetal, 10, 100, 30);
		this.maxWater = this.withinRangeOrDefault(maxWater, 10, 100, 30);
		this.maxGas = this.withinRangeOrDefault(maxGas, 10, 100, 30);
		this.maxAmmo = this.withinRangeOrDefault(maxAmmo, 10, 100, 30);
		this.maxFood = this.withinRangeOrDefault(maxFood, 10, 100, 30);

		this.fuel = this.withinRangeOrDefault(fuel, 0, this.maxFuel, this.maxFuel);
		this.metal = this.withinRangeOrDefault(metal, 0, this.maxWater, this.maxMetal);
		this.water = this.withinRangeOrDefault(water, 0, this.maxWater, this.maxWater);
		this.gas = this.withinRangeOrDefault(gas, 0, this.maxGas, this.maxGas);
		this.ammo = this.withinRangeOrDefault(ammo, 0, this.maxAmmo, this.maxAmmo);
		this.food = this.withinRangeOrDefault(food, 0, this.maxFood, this.maxFood);

		if (parts == null || parts.isEmpty()) {
			this.parts = new ArrayList<>();
		} else {
			this.parts = parts;
		}
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
		if (part == null) {
			throw new IllegalArgumentException("Must include a part to be added");
		} else {
			this.parts.add(part);
		}
	}
	public void removePart(PartModules part) {
		if (part == null) {
			throw new IllegalArgumentException("Must reference a part to be removed");
		} else {
			this.parts.remove(part);
		}
	}
	public List<PartModules> listParts() {
		return this.parts;
	}
}
