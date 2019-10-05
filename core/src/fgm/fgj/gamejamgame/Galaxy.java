package fgm.fgj.gamejamgame;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	/** Instantiates the galaxy generating a new map based on the solar system quantity unless root is provided. */
	public Galaxy(int solarSystemQuantity, SolarSystem root, Ship player, List<Species> bestiary){
		if(solarSystemQuantity < 1){
			throw new IllegalArgumentException("A galaxy must consist of at least 1 solar system.");
		}
		if(root == null){
			this.shipLocation = generateGalaxyMap(solarSystemQuantity);
		}else{
			this.shipLocation = root;
		}
		if(player == null){
			throw new IllegalArgumentException("A galaxy must have a player.");
		}else{
			this.player = player;
		}
		if(bestiary == null){
			this.bestiary = generateBestiary();
		}else if(bestiary.size() < 1){
			this.bestiary = generateBestiary();
		}else{
			this.bestiary = bestiary;
		}
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
		int nameSize = (int)(Math.random() * 4) + 1;
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
	public SolarSystem generateGalaxyMap(int solarSystemQuantity){
		/* Temporary cache for created solar systems until they are linked and a single root node can be returned. */
		List<SolarSystem> created = new ArrayList<>();
		/* Create the solar systems. */
		int ssQuantity = (int)Math.random() * 25 + 5;
		for(int i = 0; i < ssQuantity; i++){
			SolarSystem ss = this.generateSolarSystem();
			created.add(ss);
		}
		/* Link the solar systems. */
		for(SolarSystem ss : created){
			int links = (int)Math.random() * 6;
			for(int i = 0; i < links; i++){
				int linkIndex = (int)Math.random() * created.size();
				int fuelCost = (int)(Math.random() * 4) + 1;
				if(!ss.linkedSolarSystems.contains(created.get(linkIndex))){
					/* If the link doesn't already exist make it bidirectional to avoid unreachable solar systems.
					* This also ensures that both ways have the same fuelCosts. */
					ss.linkSolarSystem(created.get(linkIndex), fuelCost);
					created.get(linkIndex).linkSolarSystem(ss, fuelCost);
				}
			}
		}
		/* Return one as a starting location. */
		return created.get(0);
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
