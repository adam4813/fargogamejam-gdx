package fgm.fgj.gamejamgame;

/** Represents a being that operates the ship. */
public class CrewMember {
	/** Represents the full name of the crew member, cannot be null or "". */
	final String name;
	/** Cannot be null.
	 * @see Species */
	final Species species;
	/** Cannot be null.
	 * @see Specializations */
	final Specializations specialization;
	/** Represents the damage sustained from attacks, [0..hitPoints].
	 * @see Species#hitPoints
	 */
	private int damageTaken;

	/** Initializes a new crew member based on the properties provided.
	 * @param name {@link CrewMember#name}
	 * @param species {@link CrewMember#species}
	 * @param specialization {@link CrewMember#specialization}
	 * @param damageTaken {@link CrewMember#damageTaken}
	 */
	CrewMember (final String name, final Species species, final Specializations specialization, final int damageTaken) {
		if(name == null){
			throw new IllegalArgumentException("Crew members cannot have a null name.");
		}else if(name.equals("")){
			throw new IllegalArgumentException("Crew members cannot have an empty name.");
		}
		if(species == null){
			throw new IllegalArgumentException("Crew members need a species.");
		}
		if(specialization == null){
			throw new IllegalArgumentException("Crew members must be specialized.");
		}
		this.name = name;
		this.species = species;
		this.specialization = specialization;
		this.damageTaken = Galaxy.initializeWithConstraints(damageTaken, 0, species.hitPoints, 0);
	}

	/**
	 * @param amount represents the damage to deal to the crew member, negative values are set to 0.
	 * @return a boolean, when true, represents that the crew member died.
	 */
	boolean damage(int amount) {
		if (amount < 0) {
			amount = 0;
		}
		this.damageTaken += amount;
		return this.damageTaken >= this.species.hitPoints;
	}

	/**
	 * @param amount represents the damage to remove from the crew member, negative values are set to 0.
	 */
	void restore(int amount){
		if(amount < 0){
			amount = 0;
		}
		this.damageTaken = Math.max(this.damageTaken - amount, 0);
	}

	/** @see CrewMember#name */
	public String getName() {
		return name;
	}

	/** @see CrewMember#species */
	public Species getSpecies() {
		return species;
	}

	/** @see Species#hitPoints */
	public int getHitPoints(){
		return this.species.hitPoints;
	}

	/** @see CrewMember#damageTaken */
	public int getCurrentHitPoints() {
		return species.hitPoints - damageTaken;
	}

	/** @see CrewMember#specialization */
	public Specializations getSpecialization() {
		return specialization;
	}
}
