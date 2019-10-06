package fgm.fgj.gamejamgame;
import java.util.ArrayList;
import java.util.List;
/** Serves as a hub for encounters */
public class SolarSystem {
	String name;
	List<Planet> planets;
	int pirateThreat;
	int solarRadiation;
	int debrisRating;
	List<SolarSystem> linkedSolarSystems;
	List<Integer> fuelCosts;
	public StarType starType;
	public float starSize;

	/**
	 * Instantiates a solar system based on the provided parameters.
	 * @param name the name referred to in the info panel.
	 * @param starType the graphic displayed for the star.
	 * @param starSize the scale factor of the graphic.
	 * @param linkedSolarSystems the systems available to travel to.
	 * @param planets the bodies that can be visted for events.
	 * @param pirateThreat the chances a pirate may attack.
	 * @param solarRadiation the value the life support systems of the ship needs to exceed to be safe.
	 * @param debrisRating the chances a debris encounter may occur.
	 */
	public SolarSystem(String name, StarType starType, float starSize, List<SolarSystem> linkedSolarSystems, List<Integer> fuelCosts, List<Planet> planets, int pirateThreat, int solarRadiation, int debrisRating) {
		if(name == null){
			if(name.equals("")){
				throw new IllegalArgumentException("Solar systems must have a given name.");
			}
		}else{
			this.name = name;
		}
		if(starType == null){
			throw new IllegalArgumentException("Solar systems must have a star type.");
		}else{
			this.starType = starType;
		}
		if(starSize > .5f && starSize < 3.5f){
			this.starSize = starSize;
		}else{
			/* Assume they meant a reasonable scale factor. */
			this.starSize = 1.5f;
		}
		if(linkedSolarSystems == null){
			/* Cannot have a null list of systems the solar system is linked to. Assume they meant to have none in the list. */
			this.linkedSolarSystems = new ArrayList<>();
			this.fuelCosts = new ArrayList<>();
		}else{
			this.linkedSolarSystems = linkedSolarSystems;
			if(linkedSolarSystems.size() == fuelCosts.size()){
				/* Must have a matching amount of fuel costs to the systems connected. */
				this.fuelCosts = fuelCosts;
			}else{
				throw new IllegalArgumentException("Linked solar system and fuel cost sizes do not match!");
			}
		}

		if(planets == null){
			/* Cannot have a null list of planets the system has. Assume they meant to have none in the list. */
			this.planets = new ArrayList<>();
		}else{
			this.planets = planets;
		}
		if(pirateThreat > -1 && pirateThreat <= 5){
			this.pirateThreat = pirateThreat;
		}else{
			/* If the pirate threat is outside of the range then just assume they meant it was safe from pirates.*/
			this.pirateThreat = 0;
		}
		if(solarRadiation > -1 && solarRadiation <= 5){
			this.solarRadiation = solarRadiation;
		}else{
			/* If the solar radiation is outside of the range then just assume they meant it was average-ish. */
			this.solarRadiation = 3;
		}
		if(debrisRating > -1 && solarRadiation <= 5){
			this.debrisRating = debrisRating;
		}else{
			/* If the debris rating is outside of the range then just assume they meant it was average-ish. */
			this.debrisRating = 2;
		}
	}

	/**
	 * Adds a solar system that can be reached with the given fuel cost.
	 * @param ss a newly reachable solar system.
	 * @param fuelCost the cost in fuel to reach the solar system from the current one.
	 */
	public void linkSolarSystem(SolarSystem ss, int fuelCost){
		if(ss == null){
			throw new IllegalArgumentException("Solar Systems cannot be linked to null solar systems.");
		}
		this.linkedSolarSystems.add(ss);
		this.fuelCosts.add(fuelCost);
	}

	/**
	 * Removes a solar system that can no longer be reached from the current system. It also removes it's associated fuel cost.
	 * @param ss the solar system to remove (with its fuel cost).
	 */
	public void removeSolarSystem(SolarSystem ss){
		for(int i = 0; i < linkedSolarSystems.size(); i++){
			if(linkedSolarSystems.get(i).equals(ss)){
				linkedSolarSystems.remove(i);
				fuelCosts.remove(i);
			}
		}
	}

	public String getName() {
		return name;
	}
}
