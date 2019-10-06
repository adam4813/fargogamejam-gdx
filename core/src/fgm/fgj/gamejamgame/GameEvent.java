package fgm.fgj.gamejamgame;
import java.util.Random;

public class GameEvent {

	String eventText;
	String eventType;

	public GameEvent(SolarSystem solarSystem, Planet planet, Ship ship) {
		if (ship == null) {
			throw new IllegalArgumentException("You must provide a Ship");
		}

		Random rand = new Random();
		int randomValue = rand.nextInt(100);

		// assign probability of event

		if (randomValue < solarSystem.pirateThreat) {
			this.eventType = "pirate";
		}

		else if (randomValue < solarSystem.debrisRating + solarSystem.pirateThreat) {
			this.eventType = "debris";
		}

		else if (randomValue < solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "radiation";
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
			ship.issueHullDamage(damageAmount);
			this.eventText = "Your ship was ravaged by radiation!";
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
}
