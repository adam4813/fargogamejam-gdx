package fgm.fgj.gamejamgame;
import java.util.Random;

public class GameEvent {

	String eventText;

	public GameEvent(SolarSystem solarSystem, Planet planet, Ship ship) {
		if (ship == null) {
			throw new IllegalArgumentException("You must provide a Ship");
		}

		Random rand = new Random();
		int randomValue = rand.nextInt(100);

		// assign probability of event

		EventProbability eventProbability;

		if (randomValue < 2) {
			eventProbability = EventProbability.LOW;
		}

		else if (randomValue < 15) {
			eventProbability = EventProbability.MEDIUM;
		}

		else if (randomValue < 50) {
			eventProbability = EventProbability.HIGH;
		}

		else {
			eventProbability = EventProbability.CERTAIN;
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

		handleGameEvent(eventProbability, eventContext, solarSystem, planet, ship);
	}

	public void handleGameEvent(EventProbability eventProbability, EventContext eventContext, SolarSystem solarSystem, Planet planet, Ship ship) {
		if (eventProbability == EventProbability.LOW) {
			if (eventContext == EventContext.GALAXY) {
				this.eventText = "Galaxy context low prob";
			}

			else if (eventContext == EventContext.SOLAR_SYSTEM) {
				this.eventText = "Solar System context Low probability";
			}

			else {
				this.eventText = "Planet context Low probability";
			}
		}

		else if (eventProbability == EventProbability.MEDIUM) {
			if (eventContext == EventContext.GALAXY) {
				this.eventText = "Galaxy context Med probability";
			}

			else if (eventContext == EventContext.SOLAR_SYSTEM) {
				this.eventText = "Solar system context Med probability";
			}

			else {
				this.eventText = "Planet context Med probability";
			}
		}

		else if (eventProbability == EventProbability.HIGH) {
			if (eventContext == EventContext.GALAXY) {
				this.eventText = "Galaxy context High probability";
			}

			else if (eventContext == EventContext.SOLAR_SYSTEM) {
				this.eventText = "Solar system context High probability";
			}

			else {
				this.eventText = "Planet context High probability";
			}
		} else {
			this.eventText = "Nothing happens";
		}
	}
}
