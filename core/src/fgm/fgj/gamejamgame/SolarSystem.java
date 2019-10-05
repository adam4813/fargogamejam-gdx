package fgm.fgj.gamejamgame;
import java.util.Random;

public class SolarSystem {

	// Instance Variables

	int numberOfPlanets;
	int pirateThreat;
	int solarRadiation;
	int debrisRating;


	// constructor

	public SolarSystem() {
		Random rand = new Random();

		this.numberOfPlanets =  rand.nextInt(10);
		this.pirateThreat = rand.nextInt(3);
		this.solarRadiation = rand.nextInt(3);
		this.debrisRating = rand.nextInt(3);
	}
}
