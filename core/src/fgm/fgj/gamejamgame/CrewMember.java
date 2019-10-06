package fgm.fgj.gamejamgame;

public class CrewMember {
	final String name;
	Species species;
	Specialization specialization;

	public CrewMember (String name, Species species, Specialization specialization) {
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
	}
}
