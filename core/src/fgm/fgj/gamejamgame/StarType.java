package fgm.fgj.gamejamgame;

import java.util.Random;

public enum StarType {
	YELLOW,
	WHITE,
	BLUE,
	ORANGE,
	RED;

	public static StarType getRandomStarType() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
