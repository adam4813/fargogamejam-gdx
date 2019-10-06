package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fgm.fgj.gamejamgame.AtmosphericComposition.getRandomAtmosphere;
import static fgm.fgj.gamejamgame.WeaponType.BALLISTIC;

public class Ship {
	private int hullDamage;
	private List<CrewMember> crewMembers;
	private Engine engine;
	private Weapon weapon;
	private CargoBay cargoBay;
	private LifeSupportSystem lifeSupport;
	private int pirateThreat;

	private static void reduceThreat(Ship ship) {

	}

	public Ship() {
		this.hullDamage = 0;
		this.crewMembers = new ArrayList<>();
		this.engine = new Engine(3, 3);
		this.weapon = new Weapon(3, BALLISTIC);
		this.cargoBay = new CargoBay(50, 15, 25, 25, 100, 50);
		List<AtmosphericComposition> supportedAtmosphericCompositions = new ArrayList<>();
		supportedAtmosphericCompositions.add(getRandomAtmosphere());
		this.lifeSupport = new LifeSupportSystem(3, supportedAtmosphericCompositions, 3);
		this.pirateThreat = 0;
	}

	public void issueHullDamage(int damageAmount) {
		this.hullDamage = this.hullDamage + damageAmount;
		if (this.hullDamage > 99) {
			// game over
		}
	}

	public void repairHullDamage(int repairAmount) {
		this.hullDamage = this.hullDamage - repairAmount;
	}

	public int getRadiationResistance() {
		return this.lifeSupport.getSolarRadiationTolerance();
	}

	public int getEngineSpeed() {
		return this.engine.getSpeed();
	}

	public void plunderCargo() {
		Random rand = new Random();
		int plunderAmount = rand.nextInt(5);

		this.cargoBay.decreaseMetal(plunderAmount);
		this.cargoBay.decreaseWater(plunderAmount);
		this.cargoBay.decreaseAmmo(plunderAmount);
		this.cargoBay.decreaseFood(plunderAmount);
		this.cargoBay.decreaseFuel(plunderAmount);
	}

	public void addCargo(String cargoType, int amount) {
		switch (cargoType) {
			case "fuel":
				this.cargoBay.increaseFuel(amount);
				break;
			case "metals":
				this.cargoBay.increaseMetal(amount);
				break;
			case "water":
				this.cargoBay.increaseWater(amount);

		}

		if (cargoType == "fuel") {
			this.cargoBay.increaseFuel(amount);
		} else if (cargoType == "metals") {
			this.cargoBay.increaseMetal(amount);
		} else if (cargoType == "water") {
			this.cargoBay.increaseWater(amount);
		}
	}
}
