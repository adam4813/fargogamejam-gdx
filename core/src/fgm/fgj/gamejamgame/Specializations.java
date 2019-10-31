package fgm.fgj.gamejamgame;


/** Represents the kind of work a crew member can perform. */
public enum Specializations {
	/**
	 * Required to repair the ship.
	 * Required to swap parts.
	 * Mitigates some negative events.
	 * Boosts some positive events.
	 */
	ENGINEER(IconType.SPECIALIZATION_BROWN),
	/**
	 * Mitigates some negative events.
	 * Boosts some positive events.
	 */
	PILOT(IconType.SPECIALIZATION_BLUE),
	/**
	 * Mitigates some negative events.
	 * Boosts some positive events.
	 */
	SCIENTIST(IconType.SPECIALIZATION_YELLOW);
	/** Represents how the crew is displayed. Cannot be null. */
	IconType icon;

	/** Initializes the specializations with the appropriate icon.
	 * @param icon {@link Specializations#icon}
	 */
	Specializations(IconType icon){
		this.icon = icon;
	}

	/**
	 * @return a Specializations at random.
	 */
	public static Specializations getRandomSpecialization() {
		return values()[(int)(Math.random() * values().length)];
	}

	/** @see Specializations#icon */
	public IconType getIcon(){
		return icon;
	}
}
