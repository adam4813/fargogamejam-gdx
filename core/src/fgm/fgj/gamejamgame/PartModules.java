package fgm.fgj.gamejamgame;

/**
 * Represents components of a ship.
 */
public interface PartModules {
	/** Convenience method to check if the input is within the given constraints.
	 * @param input represents the desired value.
	 * @param min represents the lowest allowed value.
	 * @param max represents the highest allowed value.
	 * @param def represents the value if the desired value is not [min..max].
	 * @return an int equal to input if min <= input <= max, otherwise equal to def.
	 */
	static int initializeWithConstraints(int input, int min, int max, int def) {
		if (input >= min && input <= max) {
			return input;
		}
		return def;
	}

	/**
	 * @return an integer in the range of [0..4] for module display purposes.
	 */
	public int getModuleLevel();

}
