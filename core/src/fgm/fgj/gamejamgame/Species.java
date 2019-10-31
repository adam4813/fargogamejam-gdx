package fgm.fgj.gamejamgame;

/** Represents a kind of being. */
public class Species {
	/** Represents how much food is gained when killed, [0..5]. */
	final int mass;
	/** Represents the name of the species. Cannot be null or "". */
	final String name;
	/** Represents the atmospheric composition that the species is compatible with.
	 * @see AtmosphericCompositions */
	final AtmosphericCompositions atmosphericCompositionTolerance;
	/** Represents the temperature that the species is compatible with, [0..5]. */
	final int temperatureTolerance;
	/** Represents the atmospheric pressure the species is compatible with, [0..5]. */
	final int atmosphericPressureTolerance;
	/** Represents the gravity level the species is compatible with, [0..5]. */
	final int gravityTolerance;
	/** Represents the amount of damage the species can tolerate before dying, [2..10]. */
	final int hitPoints;
	/** Represents the aggressiveness or hostility and the amount of damage the species deals in some events, [0..5] */
	final int damage;
	/** Represents how the species is displayed. */
	final IconType icon;

	/** Initializes the species with the given parameters if they're within range, otherwise it uses a midpoint default.
	 * @param name {@link Species#name}
	 * @param icon {@link Species#icon}
	 * @param tolerance {@link Species#atmosphericCompositionTolerance}
	 * @param atmosphericPressureTolerance {@link Species#atmosphericPressureTolerance}
	 * @param temperatureTolerance {@link Species#temperatureTolerance}
	 * @param gravityTolerance {@link Species#gravityTolerance}
	 * @param mass {@link Species#mass}
	 * @param hitPoints {@link Species#hitPoints}
	 * @param damage {@link Species#damage}
	 */
	public Species(String name, IconType icon, AtmosphericCompositions tolerance, int atmosphericPressureTolerance, int temperatureTolerance, int gravityTolerance, int mass, int hitPoints, int damage) {
		if(name == null){
			throw new IllegalArgumentException("Species cannot be null named.");
		}else if(name == ""){
			throw new IllegalArgumentException("Species cannot be unnamed.");
		}else{
			this.name = name;
		}
		if(icon == null){
			throw new IllegalArgumentException("Species cannot have a null icon.");
		}else{
			this.icon = icon;
		}
		if(tolerance == null){
			throw new IllegalArgumentException("Species must have a compatible atmospheric composition.");
		}else{
			this.atmosphericCompositionTolerance = tolerance;
		}
		this.atmosphericPressureTolerance = Galaxy.initializeWithConstraints(atmosphericPressureTolerance, 0, 5, 2);
		this.temperatureTolerance = Galaxy.initializeWithConstraints(temperatureTolerance, 0, 5, 2);
		this.gravityTolerance = Galaxy.initializeWithConstraints(gravityTolerance, 0, 5, 2);
		this.mass = Galaxy.initializeWithConstraints(mass, 0, 5, 2);
		this.hitPoints = Galaxy.initializeWithConstraints(hitPoints, 2, 10, 6);
		this.damage = Galaxy.initializeWithConstraints(damage, 0, 5, 2);
	}

	/** @see Species#name */
	public String getName() {
		return name;
	}

	/** @see Species#icon */
	public IconType getIcon(){
		return icon;
	}
}
