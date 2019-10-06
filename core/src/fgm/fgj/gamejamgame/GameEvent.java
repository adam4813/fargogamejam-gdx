package fgm.fgj.gamejamgame;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEvent {

	String eventText;
	String eventType;
	Boolean didLose;
	Boolean didWin;

	public GameEvent(SolarSystem solarSystem, Planet planet, Ship ship) {
		if (ship == null) {
			throw new IllegalArgumentException("You must provide a Ship");
		}

		if (checkWinCondition(planet, ship)) {
			this.didWin = Boolean.TRUE;
		}

		Random rand = new Random();
		int randomValue = rand.nextInt(100);

		// assign probability of event

		if (this.didWin) {
			this.eventType = "habitablePlanet";
		}

		else if (randomValue < solarSystem.pirateThreat) {
			this.eventType = "pirate";
		}

		else if (randomValue < solarSystem.debrisRating + solarSystem.pirateThreat) {
			this.eventType = "debris";
		}

		else if (randomValue < solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "radiation";
		}

		else if (planet != null && planet.getMostViolentSpecies() != null && randomValue < planet.getMostViolentSpecies().damage + solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "ambush";
		}

		else if (randomValue < 60){
			this.eventType = "resource";
		} else {
			this.eventType = "none";
		}

		// Infer event context based on the objects passed in

		EventContext eventContext;

		if (solarSystem == null && planet == null) {
			eventContext = EventContext.GALAXY;
		}

		else if (planet == null) {
			eventContext = EventContext.SOLAR_SYSTEM;
		}

		else {
			eventContext = EventContext.PLANET;
		}

		handleGameEvent(eventType, eventContext, solarSystem, planet, ship);
	}

	private void handleGameEvent(String eventType, EventContext eventContext, SolarSystem solarSystem, Planet planet, Ship ship) {

		switch(eventType) {
			case "habitablePlanet":
				this.eventText = "You found a habitable planet! Do you want to settle?";
				break;
			case "pirate":
				handlePirateEvent(ship);
				break;

			case "debris":
				handleDebrisEvent(ship);
				break;

			case "radiation":
				handleRadiationEvent(solarSystem, ship);
				break;

			case "resource":
				if (eventContext == EventContext.PLANET) {
					handleResourceCollection(planet, ship);
				}
				break;

			case "ambush":
				if (eventContext == EventContext.PLANET) {
					handleAmbushEvent(planet, ship);
				}
				break;

			default:
				this.eventText = "Your voyage was uneventful.";

		}
	}

	private void handlePirateEvent(Ship ship) {
		Random rand = new Random();
		int outrunFactor = rand.nextInt(5);
		if (outrunFactor < ship.getEngineSpeed()) {
			this.eventText = "You encountered space pirates, but were able to escape!";
		} else {
			this.eventText = "You encountered space pirates! They took a bunch of your stuff!";
			ship.plunderCargo();
		}
	}

	private void handleDebrisEvent(Ship ship) {
		Random rand = new Random();
		int damageAmount = rand.nextInt(20);
		ship.issueHullDamage(damageAmount);
		this.eventText = "Your ship was damaged by debris! It caused" + damageAmount + " damage";
	}

	private void handleRadiationEvent(SolarSystem solarSystem, Ship ship) {
		if (solarSystem.solarRadiation > ship.getRadiationResistance()) {
			Random rand = new Random();
			int damageAmount = rand.nextInt(20);
			Boolean isShipDestroyed = ship.issueHullDamage(damageAmount);
			if (isShipDestroyed) {
				this.eventText = "Your ship was destroyed - Game Over";
				this.didLose = Boolean.TRUE;
			} else {
				this.eventText = "Your ship was ravaged by radiation!";
			}
		}
	}

	private void handleResourceCollection(Planet planet, Ship ship) {
		Random rand = new Random();
		int harvestAmount = rand.nextInt(5);

		if (planet.metals > harvestAmount) {
			planet.metals = planet.metals - harvestAmount;
			ship.addCargo("metals", harvestAmount);
		}

		if (planet.water > harvestAmount) {
			planet.water = planet.water - harvestAmount;
			ship.addCargo("water", harvestAmount);
		}

		if (planet.fuel > harvestAmount) {
			planet.fuel = planet.fuel - harvestAmount;
			ship.addCargo("fuel", harvestAmount);
		}

		this.eventText = "You collected resources!";
	}

	private Boolean checkWinCondition(Planet planet, Ship ship) {
		if (planet == null) {
			return Boolean.FALSE;
		}
		int planetGravity = planet.gravity;
		int planetAtmosphericPressure = planet.atmosphericPressure;
		int planetTemperature = planet.temperature;
		AtmosphericComposition planetAir = planet.airType;

		List<CrewMember> crewMembers = ship.crewMembers;
		CrewMember crewMember = crewMembers.get(0);
		Species crewSpecies = crewMember.species;

		int speciesGravityTolerance = crewSpecies.gravityTolerance;
		int speciesAtmosphericPressureTolerance = crewSpecies.atmosphericPressureTolerance;
		int speciesTemperatureTolerance = crewSpecies.temperatureTolerance;
		AtmosphericComposition speciesAtmosphericCompositionTolerance = crewSpecies.atmosphericCompositionTolerance;

		Boolean gravityMatch = (planetGravity == speciesGravityTolerance);
		Boolean pressureMatch = (planetAtmosphericPressure == speciesAtmosphericPressureTolerance);
		Boolean temperatureMatch = (planetTemperature == speciesTemperatureTolerance);
		Boolean airTypeMatch = (planetAir == speciesAtmosphericCompositionTolerance);

		return (gravityMatch && pressureMatch && temperatureMatch && airTypeMatch);
	}

	private void handleAmbushEvent(Planet planet, Ship ship) {
		Species ambushingSpecies = planet.getMostViolentSpecies();
		// Get random crew member to the ambush will injure or kill
		List<CrewMember> aliveCrewMembers = ship.crewMembers;
		int randomCrewMemberIndex = (int) (Math.random() * aliveCrewMembers.size());
		CrewMember randomCrewMember = aliveCrewMembers.get(randomCrewMemberIndex);
		String crewMemberName = randomCrewMember.name;
		if (randomCrewMember.dealDamage(ambushingSpecies.damage)) {
			ship.crewMembers.remove(randomCrewMember);
			this.eventText = "Crew member '" + crewMemberName + "' was killed. ";
		} else {
			this.eventText = "Crew member '" + crewMemberName + "' was injured. ";
		}

		int ammoRequired = (int) (ambushingSpecies.hitPoints / ship.getDamagePerAmmo()) * ambushingSpecies.hitPoints;
		int currentAmmo = ship.getCargoBay().checkAmmo();
		if (currentAmmo < ammoRequired) {
			this.eventText += "Ran out of ammo, no food acquired. ";
		} else {
			if (currentAmmo == ammoRequired) {
				this.eventText += "Ran out of ammo. ";
			}
			ship.getCargoBay().increaseFood(ambushingSpecies.mass);
			this.eventText += "Acquired " + ambushingSpecies.mass + " food.";
		}
		ship.getCargoBay().decreaseAmmo(ammoRequired);
	}
}
