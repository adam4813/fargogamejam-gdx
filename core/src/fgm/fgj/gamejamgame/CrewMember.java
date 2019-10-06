package fgm.fgj.gamejamgame;

public class CrewMember {
	final String name;
	final Species species;
	final Specialization specialization;
	int damageTaken;

	public CrewMember (String name, Species species, Specialization specialization, int damageTaken) {
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
		if (damageTaken < -1) {
			this.damageTaken = 0;
		} else {
			this.damageTaken = damageTaken;
		}
	}

	/**
	 * @param newDamage amount of damage to deal to the crew member
	 * @return boolean True = dead
	 */
	public boolean dealDamage(int newDamage) {
		if (damageTaken < 0) {
			throw new IllegalArgumentException("New damage must be 0 or more.");
		} else {
			this.damageTaken += newDamage;
		}
		return this.damageTaken >= this.species.hitPoints;
	}

	public String getName() {
		return name;
	}

	public Species getSpecies() {
		return species;
	}

	public int getHP() {
		return species.hitPoints - damageTaken;
	}

	public Specialization getSpecialization() {
		return specialization;
	}
}
