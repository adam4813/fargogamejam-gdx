package fgm.fgj.gamejamgame;

import java.util.Random;

public enum IconType {
	SOLAR_SYSTEM_BUTTON,
	STARMAP_BUTTON,
	INFO_BUTTON,
	SHIP_EQUIP_BUTTON,
	BUTTON1,
	BUTTON2,
	BUTTON3,
	BUTTON4,
	BUTTON5,
	SHIP_INFO_BUTTON,
	PLAY_BUTTON,
	EXIT_BUTTON,
	SPECIES_DARK_PURPLE,
	SPECIES_WHITE,
	SPECIES_GREEN,
	SPECIES_BROWN,
	SPECIES_TEAL,
	SPECIES_BLACK,
	SPECIES_LIGHT_BLUE,
	SPECIES_DARK_BLUE,
	SPECIES_PINK,
	SPECIES_PURPLE,
	RESOURCE_YELLOW,
	RESOURCE_BROWN,
	RESOURCE_BLUE,
	RESOURCE_GRAY,
	RESOURCE_GREEN,
	RESOURCE_PURPLE,
	SPECIALIZATION_BROWN,
	SPECIALIZATION_BLUE,
	SPECIALIZATION_YELLOW;


	public static IconType getRandomSpecies() {
		Random random = new Random();
		return values()[random.nextInt(10) + 11];
	}

	public static IconType getRandomSpecialization() {
		Random random = new Random();
		return values()[random.nextInt(3) + 26];
	}
}
