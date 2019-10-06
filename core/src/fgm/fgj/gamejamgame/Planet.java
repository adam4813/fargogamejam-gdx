package fgm.fgj.gamejamgame;
import java.util.ArrayList;
import java.util.List;

public class Planet {
	public final PlanetType planetType;
	final String name;
	final AtmosphericComposition airType;
	final List<Species> speciesPresent;
	final int gravity;
	final int atmosphericPressure;
	final int temperature;
	int fuel;
	int metals;
	int water;

	/**
	 * Instantiates a new planet based on parameters given.
	 * @param name the name of the planet
	 * @param airType what the atmosphere composition is.
	 * @param speciesPresent which species from the bestiary are present here.
	 * @param gravity the gravity level of the planet.
	 * @param atmosphericPressure the pressure level of the planet.
	 * @param temperature the temperature level of the planet.
	 * @param fuel the exploitable fuels available on the planet.
	 * @param metals the exploitable metals available on the planet.
	 * @param water the exploitable water available on the planet.
	 */
	public Planet(String name, AtmosphericComposition airType, List<Species> speciesPresent, int gravity, int atmosphericPressure, int temperature, int fuel, int metals, int water) {
		this.planetType = PlanetType.getRandomPlanetType();
		if(name != null){
			if(!name.equals("")){
				/* Just ensuring the name isn't empty. */
				this.name = name;
			}else{
				throw new IllegalArgumentException("A planet must have a name.");
			}
		}else{
			throw new IllegalArgumentException("A planet must have a non null name.");
		}
		if(airType == null){
			throw new IllegalArgumentException("A planet must have a atmospheric composition.");
		}else{
			this.airType = airType;
		}
		if(speciesPresent == null){
			/* Assume they meant to have no species instead of creating an invalid planet. */
			this.speciesPresent = new ArrayList<>();
		}else{
			this.speciesPresent = speciesPresent;
		}
		if(gravity > -1 && gravity <= 5){
			this.gravity = gravity;
		}else{
			/* Assume they meant average gravity.*/
			this.gravity = 2;
		}
		if(atmosphericPressure > -1 && atmosphericPressure <= 5){
			this.atmosphericPressure = atmosphericPressure;
		}else{
			/* Assume they meant average pressure. */
			this.atmosphericPressure = 3;
		}
		if(temperature > -1 && temperature <= 5){
			this.temperature = temperature;
		}else{
			/* Assume they meant average temperature. */
			this.temperature = 2;
		}
		if(fuel < 0 || fuel > 125){
			/* Assume the fuel levels are nonexistant if they gave a bad value. */
			this.fuel = 0;
		}else{
			this.fuel = fuel;
		}
		if(metals < 0 || metals > 125){
			this.metals = 0;
		}else{
			this.metals = metals;
		}
		if(water < 0 || water > 125){
			this.water = 0;
		}else{
			this.water = water;
		}
	}

	public String getName() {
		return name;
	}

	public Species getMostViolentSpecies() {
		Species mostViolentSpecies = null;
		if (this.speciesPresent.size() > 0) {
			for (Species species : this.speciesPresent) {
				if (mostViolentSpecies == null || species.damage > mostViolentSpecies.damage) {
					mostViolentSpecies = species;
				}
			}
		}
		return mostViolentSpecies;
	}

	public Boolean isHabitablePlanet(Ship ship) {
		if(ship == null){
			throw new IllegalArgumentException("A planet cannot be habitable with a null ship.");
		}
		boolean isHabitable = true;
		List<CrewMember> crewMembers = ship.listCrewMembers();
		for(CrewMember cm : crewMembers){
			Species s = cm.species;
			isHabitable &= this.gravity == s.gravityTolerance;
			isHabitable &= this.atmosphericPressure == s.atmosphericPressureTolerance;
			isHabitable &= this.temperature == s.temperatureTolerance;
			isHabitable &= this.airType == s.atmosphericCompositionTolerance;
			if(!isHabitable){
				break;
			}
		}
		return isHabitable;
	}
}
