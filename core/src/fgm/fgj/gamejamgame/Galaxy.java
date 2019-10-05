package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Represents the world map of the game. */
public class Galaxy {
	/** Represents the node that the ship is at, also serving as a root node for the solar systems. */
	private SolarSystem shipLocation;
	private Ship player;
	public List<Species> bestiary;
	private static List<String> nameParts;
	static{
		nameParts = new ArrayList<>();
		nameParts.add("GA");
		nameParts.add("GE");
		nameParts.add("GI");
		nameParts.add("GO");
		nameParts.add("GU");
	}
	/** Instantiates the galaxy generating a new map based on the solar system quantity. */
	public Galaxy(int solarSystemQuantity){
		this.shipLocation = generateGalaxyMap();
		this.bestiary = generateBestiary();
	}

	/** @return the player/ship status. */
	public Ship getShip(){
		return player;
	}

	/** @return the root node of the galaxy */
	public SolarSystem getShipLocation(){
		return shipLocation;
	}

	/**
	 * Builds a rudimentary name for stuff.
	 * @return a name in a format like GEGU-874
	 */
	public static String generateName(){
		StringBuilder solarName = new StringBuilder();
		int nameSize = (int)(Math.random() * 5);
		for(int i = 0; i < nameSize; i++){

			solarName.append(Galaxy.nameParts.get((int)(Math.random() * nameParts.size())));
		}
		solarName.append('-');
		solarName.append((int)(Math.random() * 1000));
		return solarName.toString();
	}

	/**
	 *
	 * @return a randomly generated solar system WITHOUT links to other systems.
	 */
	public SolarSystem generateSolarSystem(){
		String name = Galaxy.generateName();
		StarType starType = StarType.getRandomStarType();
		float starSize = (float)(Math.random() * 3) + .5f;
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int planetQuantity = (int)Math.random() * 15;
		for(int i = 0; i < planetQuantity; i++){
			planets.add(this.generatePlanet());
		}
		int pirateThreat = (int) Math.random() * 6;
		int solarRadiation = (int) Math.random() * 6;
		int debrisRating = (int) Math.random() * 6;
		return new SolarSystem(name, starType, starSize, links, fuelCosts, planets, pirateThreat, solarRadiation, debrisRating);
	}

	/**
	 * Generates a galaxy by linking a bunch of solar systems.
	 * @return a root solar system that the galaxy can be traversed from.
	 */
	public SolarSystem generateGalaxyMap(){
		return generateSolarSystem();
	}

	/**
	 * Builds a bestiary of species available in the galaxy.
	 * @return
	 */
	private List<Species> generateBestiary() {
		List<Species> speciesList = new ArrayList();
		for(int i=0; i==5; i++){
			speciesList.add(new Species());
		}
		return speciesList;
	}

	/**
	 * Convenience function to get a random species of those available.
	 * @return
	 */
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

	/**
	 * Generates a random planet and populates it with species from the bestiary.
	 * @return a planet.
	 */
	public Planet generatePlanet() {
		String name = Galaxy.generateName();
		AtmosphericComposition airType = AtmosphericComposition.getRandomAtmosphere();
		List<Species> speciesPresent = new ArrayList<>();
		int speciesPresentQuantity = (int)Math.random() * 3;
		for(int i = 0; i < speciesPresentQuantity; i++){
			speciesPresent.add(this.getRandomSpeciesFromBestiary());
		}
		int gravity = (int)Math.random() * 6;
		int atmospherePressure = (int)Math.random() * 6;
		int temperature = (int)Math.random() * 6;
		int fuel = (int)Math.random() * 126;
		int metals = (int)Math.random() * 126;
		int water = (int)Math.random() * 126;
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure, temperature, fuel, metals, water);
	}
}
