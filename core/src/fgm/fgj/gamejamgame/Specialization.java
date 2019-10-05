package fgm.fgj.gamejamgame;

import java.util.Random;

public enum Specialization {
	ENGINEER,
	PILOT,
	SCIENTIST;

	public static Specialization getRandomSpecialization() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
