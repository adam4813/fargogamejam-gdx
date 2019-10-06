package fgm.fgj.gamejamgame;

public class CrewMember {
	Species species;
	Specialization specialization;

	public CrewMember (Species species, Specialization specialization) {
		if(species == null){
			throw new IllegalArgumentException("Crew members need a species.");
		}
		if(specialization == null){
			throw new IllegalArgumentException("Crew members must be specialized.");
		}
		this.species = species;
		this.specialization = specialization;
	}
}
