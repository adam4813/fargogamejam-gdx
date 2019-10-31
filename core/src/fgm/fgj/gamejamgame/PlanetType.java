package fgm.fgj.gamejamgame;

/** Represents how planets are rendered. */
public enum PlanetType {
	@SuppressWarnings("javadocs")
	BARREN,
	@SuppressWarnings("javadocs")
	EARTH_LIKE,
	@SuppressWarnings("javadocs")
	GAS1,
	@SuppressWarnings("javadocs")
	GAS2,
	@SuppressWarnings("javadocs")
	GAS3;

	/**
	 * @return a PlanetType at random.
	 */
	public static PlanetType getRandomPlanetType() {
		return values()[(int)(Math.random() * values().length)];
	}
}
