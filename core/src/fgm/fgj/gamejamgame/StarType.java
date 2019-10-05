package fgm.fgj.gamejamgame;

import java.util.Random;

/**
 * Base star types, BG types are for the background only.
 */
public enum StarType {
	YELLOW,
	WHITE,
	BLUE,
	ORANGE,
	RED,
	BG_YELLOW,
	BG_BROWN,
	BG_BLUE,
	BG_GREEN,
	BG_RED;;

	public static StarType getRandomStarType() {
		Random random = new Random();
		return values()[random.nextInt(values().length - 5)];
	}
}
