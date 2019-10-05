package fgm.fgj.gamejamgame;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.Arrays;
import fgm.fgj.gamejamgame.Species;

public class Planet {

	// instance variables

	int fuel;
	int metals;
	int water;
	int[] temperatureRange;
	int minTemperature;
	int maxTemperature;
	int atmosphericPressure;
	int gravity;
	String[] atmosphericComposition;
	Species[] speciesPresent;

	// constructor

	public Planet() {
		Random rand = new Random();

		this.gravity = rand.nextInt(3);
		this.atmosphericPressure = rand.nextInt(3);
		this.temperatureRange = getTemperatureRange();
		this.minTemperature = this.temperatureRange[0];
		this.maxTemperature = this.temperatureRange[1];
		this.fuel = rand.nextInt(10000);
		this.metals = rand.nextInt(10000);
		this.water = rand.nextInt(10000);
//		this.speciesPresent = getSpecies();
	}

	private static int[] getTemperatureRange() {
		Random rand = new Random();

		int[] temperatureRange = new int[2];
		temperatureRange[0] = rand.nextInt(1000);
		temperatureRange[1] = rand.nextInt(1000);
		Arrays.sort(temperatureRange);
		return temperatureRange;
	}

//	private static Species[] getSpecies() {
//		// TODO: generate species array here
//	}
}
