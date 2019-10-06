package fgm.fgj.gamejamgame;

import java.util.Random;

public enum WeaponType {
	BALLISTIC,
	PLASMA;

	public static WeaponType getRandomWeaponType() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
