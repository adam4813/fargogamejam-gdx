package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/** Represents the world map of the game. */
public class Galaxy {
	/** Represents the node that the ship is at, also serving as a root node for the solar systems. */
	private SolarSystem shipLocation;
	public List<Species> bestiary;

	/**
	 * Instantiates the galaxy generating a new map based on the solar system quantity.
	 */
	public Galaxy(int solarSystemQuantity){
		this.shipLocation = generateGalaxyMap();
		this.bestiary = generateBestiary();
	}

	/** Returns the root node of the galaxy */
	public SolarSystem getShipLocation(){
		return shipLocation;
	}


	public static SolarSystem generateSolarSystem(){
		String name = "Sun";
		StarType starType = StarType.BG_YELLOW;
		float starSize = 1;
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int pt = 0;
		int sr = 0;
		int dr = 0;
		return new SolarSystem(name, starType, starSize, links, fuelCosts, planets, pt, sr, dr);
	}

	public static SolarSystem generateGalaxyMap(){
		return generateSolarSystem();
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

	public static Planet generatePlanet() {
		String name = "Earth";
		AtmosphericComposition airType = AtmosphericComposition.EARTH_LIKE;
		List<Species> speciesPresent = new ArrayList<>();
		int gravity = 2;
		int atmospherePressure = 2;
		int temperature = 2;
		int fuel = 50;
		int metals = 40;
		int water = 100;
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure, temperature, fuel, metals, water);
	}
}
