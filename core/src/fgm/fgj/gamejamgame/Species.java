package fgm.fgj.gamejamgame;
import java.util.Arrays;
import java.util.Random;

public class Species {

	// instance variables
	boolean isFood;
	boolean isSentient;
	boolean isHostile;
	int mass;
	String name;
	int[] temperatureToleranceRange;
	int minTemperatureTolerance;
	int maxTemperatureTolerance;
	String[] atmosphericComposition;
	int[] atmosphericPressureToleranceRange;
	int minAtmosphericPressureTolerance;
	int maxAtmosphericPressureTolerance;
	int[] gravityToleranceRange;
	int maxGravityTolerance;
	int minGravityTolerance;
	int hitpoints;
	int damage;

	// constructor
	public Species() {
		Random rand = new Random();
		this.isFood = rand.nextBoolean();
		this.isSentient = rand.nextBoolean();
		this.isHostile = rand.nextBoolean();
		this.mass = rand.nextInt(10000);

		this.temperatureToleranceRange = getTemperatureToleranceRange();
		this.minTemperatureTolerance = this.temperatureToleranceRange[0];
		this.maxTemperatureTolerance = this.temperatureToleranceRange[1];

		this.atmosphericComposition = getAtmosphericComposition();

		this.atmosphericPressureToleranceRange = getPressureToleranceRange();
		this.minAtmosphericPressureTolerance = this.atmosphericPressureToleranceRange[0];
		this.maxAtmosphericPressureTolerance = this.atmosphericPressureToleranceRange[1];

		this.gravityToleranceRange = getGravityToleranceRange();
		this.minGravityTolerance = this.gravityToleranceRange[0];
		this.maxGravityTolerance = this.gravityToleranceRange[1];

		this.hitpoints = rand.nextInt(100);
		this.damage = 0;
	}

	// methods

	private int[] getTemperatureToleranceRange() {

	}

	private int[] getPressureToleranceRange() {

	}

	private int[] getGravityToleranceRange() {

	}

	private String[] getAtmosphericComposition() {

	}
}
