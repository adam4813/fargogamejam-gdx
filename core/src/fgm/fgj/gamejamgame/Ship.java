package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

import static fgm.fgj.gamejamgame.AtmosphericComposition.EARTH_LIKE;
import static fgm.fgj.gamejamgame.AtmosphericComposition.THICK;
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
		this.cargoBay = new CargoBay(25, 25, 25, 25, 25, 25);
		List<AtmosphericComposition> supportedAtmosphericCompositions = new ArrayList<>();
		supportedAtmosphericCompositions.add(EARTH_LIKE);
		supportedAtmosphericCompositions.add(THICK);
		this.lifeSupport = new LifeSupportSystem(3, supportedAtmosphericCompositions, 3);
		this.pirateThreat = 0;
	}
}
