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

	public CargoBay (int maxFuel, int maxMetal, int maxWater, int maxGas, int maxAmmo, int maxFood) {
		this.fuel = 0;
		this.maxFuel = maxFuel;
		this.metal = 0;
		this.maxMetal = maxMetal;
		this.water = 0;
		this.maxWater = maxWater;
		this.gas = 0;
		this.maxGas = maxGas;
		this.parts = new ArrayList<>();
		this.ammo = 0;
		this.maxAmmo = maxAmmo;
		this.food = 0;
		this.maxFood = maxFood;
	}

	public void increaseFuel(int newFuel) {
		this.fuel = this.fuel + newFuel > this.maxFuel ? this.maxFuel : this.fuel + newFuel;
	}

	public void reduceFuel(int usedFuel) {
		this.fuel = this.fuel - usedFuel < 0 ? 0 : this.fuel - usedFuel;
	}

	public int getFuel() {
		return this.fuel;
	}
}
