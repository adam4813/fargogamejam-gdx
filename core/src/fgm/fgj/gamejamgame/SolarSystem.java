package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/** Represents a celestial body that serves as a hub for planets. */
public class SolarSystem {
	/** Represents the identifier of the solar system, cannot be null or "". */
	private final String name;
	/** Represents the solar system's orbiting planets, cannot be null. */
	private final List<Planet> planets;
	/** Represents the solar system's risk for pirate encounters, [0..5]. */
	int pirateThreat;
	/** Represents the solar system's risk of radiation events, [0..5]. */
	final int solarRadiation;
	/** Represents the solar system's risk of debris events, [0..5]. */
	final int debrisRating;
	/** Represents the solar system's neighbors, cannot be null. */
	final List<SolarSystem> linkedSolarSystems;
	/** Represents the solar system's neighbors' fuel costs to travel there, cannot be null, must be the same size as {@link SolarSystem#linkedSolarSystems}. */
	final List<Integer> fuelCosts;
	/** Represents how the star in the solar system is rendered. */
	public final StarType starType;
	/** Represents the scaling factor for the star as it is rendered. */
	public final float starSize;

	/** Instantiates a solar system based on the provided parameters unless it is out of range, then midpoint defaults are used.
	 * @param name {@link SolarSystem#name}
	 * @param starType {@link SolarSystem#starType}
	 * @param starSize {@link SolarSystem#starSize}
	 * @param linkedSolarSystems {@link SolarSystem#linkedSolarSystems}
	 * @param planets {@link SolarSystem#planets}
	 * @param pirateThreat {@link SolarSystem#pirateThreat}
	 * @param solarRadiation {@link SolarSystem#solarRadiation}
	 * @param debrisRating {@link SolarSystem#debrisRating}
	 */
	SolarSystem(String name, StarType starType, float starSize, List<SolarSystem> linkedSolarSystems, List<Integer> fuelCosts, List<Planet> planets, int pirateThreat, int solarRadiation, int debrisRating) {
		if(name == null){
			throw new IllegalArgumentException("Solar systems cannot have a null name.");
		}
		if(name.equals("")){
			throw new IllegalArgumentException("Solar systems must have a given name.");
		}
		if(starType == null){
			throw new IllegalArgumentException("Solar systems must have a star type.");
		}
		if(starSize < .5f && starSize > 3.5f){
			starSize = 1.5f;
		}
		if(linkedSolarSystems == null){
			linkedSolarSystems = new ArrayList<>();
			fuelCosts = new ArrayList<>();
		}
		if(linkedSolarSystems.size() != fuelCosts.size()){
			throw new IllegalArgumentException("Linked solar system and fuel cost sizes do not match!");
		}
		if(planets == null){
			planets = new ArrayList<>();
		}
		this.name = name;
		this.starType = starType;
		this.starSize = starSize;
		this.linkedSolarSystems = linkedSolarSystems;
		this.fuelCosts = fuelCosts;
		this.planets = planets;
		this.pirateThreat = Galaxy.initializeWithConstraints(pirateThreat, 0, 5, 2);
		this.solarRadiation = Galaxy.initializeWithConstraints(solarRadiation, 0, 5, 2);
		this.debrisRating = Galaxy.initializeWithConstraints(debrisRating, 0, 5, 2);
	}

	/** Adds a solar system that can be reached with the given fuel cost.
	 * @param ss a newly reachable solar system, does nothing when null.
	 * @param fuelCost the cost in fuel to reach the solar system from the current one, does nothing when less than 1.
	 */
	void linkSolarSystem(SolarSystem ss, int fuelCost){
		if(ss != null && fuelCost > 0){
			this.linkedSolarSystems.add(ss);
			this.fuelCosts.add(fuelCost);
		}
	}

	/** Removes a solar system that can no longer be reached from the current system. It also removes it's associated fuel cost.
	 * @param ss the solar system to remove (with its fuel cost), does nothing when null.
	 */
	public void removeSolarSystem(SolarSystem ss){
		if(ss != null){
			for(int i = 0; i < linkedSolarSystems.size(); i++){
				if(linkedSolarSystems.get(i).equals(ss)){
					linkedSolarSystems.remove(i);
					fuelCosts.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * @see SolarSystem#name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return an unmodifiable list of planets that represents the planets orbiting within the solar system.
	 * @see SolarSystem#planets
	 */
	public List<Planet> getPlanets() {
		return Collections.unmodifiableList(planets);
	}
}
