package fgm.fgj.gamejamgame;

import java.util.Random;

public enum Specialization {
	ENGINEER(IconType.SPECIALIZATION_BROWN),
	PILOT(IconType.SPECIALIZATION_BLUE),
	SCIENTIST(IconType.SPECIALIZATION_YELLOW);
	IconType icon;

	private Specialization(IconType icon){
		this.icon = icon;
	}

	public static Specialization getRandomSpecialization() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}

	public IconType getIcon(){
		return icon;
	}
}
