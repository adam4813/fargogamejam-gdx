package fgm.fgj.gamejamgame;

import java.util.Random;

/**
 * Base star types, BG types are for the background only.
 */
public enum PlanetType {
	BARREN,
	EARTH_LIKE,
	GAS1,
	GAS2,
	GAS3;

	public static PlanetType getRandomPLanetType() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
