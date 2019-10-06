package fgm.fgj.gamejamgame;

import java.util.List;
import java.util.Random;

public class Ship {
	private int hullDamage;
	private List<CrewMember> crewMembers;
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
	public List<CrewMember> listCrewMembers() {
		return this.crewMembers;
	}
	public void removeCrewMember(CrewMember crewMember) {
		if (crewMember == null) {
			throw new IllegalArgumentException("Must reference the crew member that is to be removed.");
		} else {
			this.crewMembers.remove(crewMember);
		}
	}
}
