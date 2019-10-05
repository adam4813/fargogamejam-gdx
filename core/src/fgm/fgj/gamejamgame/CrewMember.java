package fgm.fgj.gamejamgame;

public class CrewMember {
	private Species species;
	private int hitPoints;
	private Specialization specialization;

	public CrewMember (Species species, int hitPoints, Specialization specialization) {
		this.species = species;
		this.hitPoints = hitPoints;
		this.specialization = specialization;
	}
}
