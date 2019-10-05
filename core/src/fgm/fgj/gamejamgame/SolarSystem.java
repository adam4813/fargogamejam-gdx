package fgm.fgj.gamejamgame;
import java.util.Random;

public class SolarSystem {
	public StarType starType;
	public float starSize = 1;

	// Instance Variables

	int numberOfPlanets;
	int pirateThreat;
	int solarRadiation;
	int debrisRating;


	// constructor

	public SolarSystem(StarType starType, float starSize) {
		Random rand = new Random();

		this.starSize = starSize;
		this.starType = starType;
		this.numberOfPlanets =  rand.nextInt(10);
		this.pirateThreat = rand.nextInt(3);
		this.solarRadiation = rand.nextInt(3);
		this.debrisRating = rand.nextInt(3);
	}
}
