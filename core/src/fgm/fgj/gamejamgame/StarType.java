package fgm.fgj.gamejamgame;

import java.util.Random;

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
