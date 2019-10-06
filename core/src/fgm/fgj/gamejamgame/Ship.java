package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fgm.fgj.gamejamgame.AtmosphericComposition.getRandomAtmosphere;
import static fgm.fgj.gamejamgame.WeaponType.BALLISTIC;

public class Ship {
	private int hullDamage;
	List<CrewMember> crewMembers;
	private Engine engine;
	private Weapon weapon;
	private CargoBay cargoBay;
	private LifeSupportSystem lifeSupport;

	public Ship(List<CrewMember> crewMembers, Engine engine, Weapon weapon, LifeSupportSystem lss, CargoBay cargoBay) {
		if(crewMembers == null){
			throw new IllegalArgumentException("A ship cannot be operated with a null crew.");
		}else if(crewMembers.isEmpty()){
			throw new IllegalArgumentException("A ship cannot be operated with an empty crew.");
		}
		if(engine == null) {
			throw new IllegalArgumentException("A ship cannot be fly with a null engine.");
		}
		if(weapon == null){
			throw new IllegalArgumentException("A ship cannot be complete with a null weapon.");
		}
		if(lss == null){
			throw new IllegalArgumentException("A ship cannot host a crew with a null life support system.");
		}
		if(cargoBay == null){
			throw new IllegalArgumentException("A ship cannot be complete with a null cargo bay.");
		}
		this.crewMembers = crewMembers;
		this.engine = engine;
		this.weapon = weapon;
		this.lifeSupport = lss;
		this.cargoBay = cargoBay;
	}

	public boolean issueHullDamage(int damageAmount) {
		this.engine.damageTaken = this.engine.damageTaken + damageAmount;
		if (this.engine.damageTaken > this.engine.hitPoints) {
			return true;
		}
		return false;
	}

	public int getDamagePerAmmo(){
		return this.weapon.damage;
	}

	public void repairHullDamage(int repairAmount) {
		this.engine.damageTaken = Math.max(this.engine.damageTaken - repairAmount, 0);
	}

	public int getRadiationResistance() {
		return this.lifeSupport.solarRadiationTolerance;
	}

	public int getEngineSpeed() {
		return this.engine.speed;
	}

	public int getEngineEfficiency(){
		return this.engine.efficiency;
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
				break;
		}
	}

	public CargoBay getCargoBay() {
		return this.cargoBay;
	}
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
}
