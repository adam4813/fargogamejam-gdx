package fgm.fgj.gamejamgame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Species {

	// instance variables
	private boolean isFood;
	private boolean isSentient;
	private boolean isHostile;
	private int mass;
	private String name;
	AtmosphericComposition atmosphericCompositionTolerance;
	private int temperatureTolerance;
	private String[] atmosphericComposition;
	private int atmosphericPressureTolerance;
	private int gravityTolerance;
	private int hitPoints;
	private int damage;

	// constructor
	public Species() {
		Random rand = new Random();
		this.isFood = rand.nextBoolean();
		this.isSentient = rand.nextBoolean();
		this.isHostile = rand.nextBoolean();
		this.mass = rand.nextInt(10000);

		this.temperatureTolerance = rand.nextInt(5);
		this.gravityTolerance = rand.nextInt(5);
		this.atmosphericPressureTolerance = rand.nextInt(5);
		this.atmosphericCompositionTolerance = getAtmosphericCompositionTolerance();

		this.hitPoints = rand.nextInt(80) + 20;
		this.damage = 0;
	}

	// methods

	private AtmosphericComposition getAtmosphericCompositionTolerance() {
		return AtmosphericComposition.getRandomAtmosphere();
	}

	public int getHitPoints() {
		return this.hitPoints;
	}
}
