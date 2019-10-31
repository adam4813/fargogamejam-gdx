package fgm.fgj.gamejamgame;

/** Represents the kinds of atmosphere compositions that planets have and species are compatible with. */
enum AtmosphericCompositions {
	TOXIC,
	EARTH_LIKE,
	NONE,
	THIN,
	THICK;

	/** @return an AtmosphericCompositions at random. */
	static AtmosphericCompositions getRandomAtmosphere() {
		return values()[(int)(Math.random() * values().length)];
	}
}


