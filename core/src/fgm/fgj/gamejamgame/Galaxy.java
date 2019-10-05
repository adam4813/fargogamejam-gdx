package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

/** Represents the world map of the game. */
public class Galaxy {
	/** Represents the node that the ship is at, also serving as a root node for the solar systems. */
	private SolarSystem shipLocation;

	/**
	 * Instantiates the galaxy generating a new map based on the solar system quantity.
	 */
	public Galaxy(int solarSystemQuantity){
		this.shipLocation = generateGalaxyMap();
	}

	/** Returns the root node of the galaxy */
	public SolarSystem getShipLocation(){
		return shipLocation;
	}


	public static SolarSystem generateSolarSystem(){
		String name = "Sun";
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int pt = 0;
		int sr = 0;
		int dr = 0;
		return new SolarSystem(name, links, fuelCosts, planets, pt, sr, dr);
	}

	public static SolarSystem generateGalaxyMap(){
		return generateSolarSystem();
	}

	public static Planet generatePlanet(){
		String name = "Earth";
		AtmosphericComposition airType = AtmosphericComposition.EARTH_LIKE;
		List<Species> speciesPresent = new ArrayList<>();
		int gravity = 2;
		int atmospherePressure = 2;
		int temperature = 2;
		int fuel = 50;
		int metals = 40;
		int water = 100;
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure,temperature, fuel, metals, water);
	}
}
