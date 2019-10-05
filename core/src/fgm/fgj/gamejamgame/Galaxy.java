package fgm.fgj.gamejamgame;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Galaxy {
	private SolarSystem solarSystem;
	private List<SolarSystem> solarSystems;
	private static SolarSystem shipLocation;
	public List<Species> bestiary;

	public Galaxy () {
		this.bestiary = generateBestiary();
	}

	private List<Species> generateBestiary() {
		List<Species> speciesList = new ArrayList();
		for(int i=0; i==5; i++){
			speciesList.add(new Species());
		}
		return speciesList;
	}

	public Species getRandomSpeciesFromBestiary() {
		Random random = new Random();
		return this.bestiary.get(random.nextInt(this.bestiary.size()));
	}

	private CrewMember generateCrewMember() {
		Species species = getRandomSpeciesFromBestiary();
		int hitPoints = species.getHitPoints();
		Specialization specialization = Specialization.getRandomSpecialization();
		CrewMember newCrewMember = new CrewMember(species, hitPoints, specialization);
		return newCrewMember;
	}
}
