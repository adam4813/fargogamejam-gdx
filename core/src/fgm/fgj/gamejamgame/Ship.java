package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

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
	private static void reduceThreat(Ship ship){

	}

	public Ship () {
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
	}

	public void repairHullDamage(int repairAmount) {
		this.hullDamage = this.hullDamage - repairAmount;
	}
}
