package fgm.fgj.gamejamgame;

import java.util.List;

public class Ship {
	private List<CrewMember> crewMembers;
	private Engine engine;
	private Weapon weapon;
	private CargoBay cargoBay;

	public Engine getEngine() {
		return engine;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public LifeSupportSystem getLifeSupport() {
		return lifeSupport;
	}

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

	public void removeCargo(String cargoType, int amount) {
		switch (cargoType) {
			case "fuel":
				this.cargoBay.decreaseFuel(amount);
				break;
			case "metals":
				this.cargoBay.decreaseMetal(amount);
				break;
			case "water":
				this.cargoBay.decreaseWater(amount);
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

	public Species getCrewSpecies() {
		List<CrewMember> crewMembers = this.listCrewMembers();
		CrewMember crewMember = crewMembers.get(0);
		return crewMember.species;
	}

	public void addCrewMember(int amount) {
		for (int i = 1; i <= amount; i++) {
			this.crewMembers.add(new CrewMember(Galaxy.generateCrewName(), getCrewSpecies(), Specialization.getRandomSpecialization(), 0));
		}
	}
}
