package fgm.fgj.gamejamgame;
import java.util.Random;

public enum AtmosphericComposition {
	TOXIC,
	EARTH_LIKE,
	NONE,
	THIN,
	THICK;

	/**
	 * Pick a random value of the AtmosphericComposition enum.
	 * @return a random AtmosphericComposition.
	 */
	public static AtmosphericComposition getRandomAtmosphere() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}


