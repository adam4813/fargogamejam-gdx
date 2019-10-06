package fgm.fgj.gamejamgame;

import java.util.List;
import java.util.Random;

public class GameEvent {
	final EventContext eventContext;
	String eventText;
	final String eventType;
	boolean didLose;
	final boolean didWin;

	public GameEvent(SolarSystem solarSystem, Planet planet, Ship ship) {
		if(solarSystem == null && planet == null){
			this.eventContext = EventContext.GALAXY;
		}else if(planet == null){
			this.eventContext = EventContext.SOLAR_SYSTEM;
		}else{
			this.eventContext = EventContext.PLANET;
		}
		if (ship == null) {
			throw new IllegalArgumentException("A game event cannot occur with a null ship.");
		}
		this.didWin = GameEvent.isHabitablePlanet(planet, ship);
		int randomValue = (int)(Math.random() * 100);
		if (this.didWin) {
			this.eventType = "habitablePlanet";
		}else if(randomValue < solarSystem.pirateThreat) {
			this.eventType = "pirate";
		}else if(randomValue < solarSystem.debrisRating + solarSystem.pirateThreat) {
			this.eventType = "debris";
		}else if(randomValue < solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "radiation";
		}else if(planet != null && planet.getMostViolentSpecies() != null && randomValue < planet.getMostViolentSpecies().damage + solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "ambush";
		}else if (randomValue < 40) {
			this.eventType = "resource";
		}else if (randomValue < 60) {
			this.eventType = "other";
		}else {
			this.eventType = "none";
		}
		handleGameEvent(this.eventType, this.eventContext, solarSystem, planet, ship);
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
			case "other":
				handleRandomEvent(eventContext, solarSystem, planet, ship);
				break;
			default:
				this.eventText = "Your voyage was uneventful.";
				break;
		}
	}

	private void handlePirateEvent(Ship ship) {
		Random rand = new Random();
		int outrunFactor = rand.nextInt(5);
		int maneuverBonus = 0;
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.PILOT)){
				/* Each pilot increases the maneuverability of the ship. */
				maneuverBonus++;
			}
		}
		if (outrunFactor < (ship.getEngineSpeed() + maneuverBonus)) {
			this.eventText = "You encountered space pirates, but were able to escape!";
		} else {
			this.eventText = "You encountered space pirates! They took a bunch of your stuff!";
			ship.getCargoBay().decreaseMetal((int)(Math.random() * 6));
			ship.getCargoBay().decreaseWater((int)(Math.random() * 6));
			ship.getCargoBay().decreaseAmmo((int)(Math.random() * 6));
			ship.getCargoBay().decreaseFood((int)(Math.random() * 6));
			ship.getCargoBay().decreaseFuel((int)(Math.random() * 6));
		}
	}

	private void handleDebrisEvent(Ship ship) {
		// If crew has pilot, reduce potential damage amount
		int maxPotentialDamage = 8;
		int damageDealt;
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.PILOT)){
				maxPotentialDamage--;
			}
		}
		damageDealt = (int)(Math.random() * maxPotentialDamage);
		Boolean isShipDestroyed = ship.issueHullDamage(damageDealt);
		if (isShipDestroyed) {
			this.eventText = "Your ship took " + damageDealt + " damage from debris and was destroyed!";
			this.didLose = true;
		} else {
			this.eventText = "Your ship took " + damageDealt + " damage from debris!";
		}
	}

	private void handleRadiationEvent(SolarSystem solarSystem, Ship ship) {
		int maxPotentialDamage = solarSystem.solarRadiation + 3 - ship.getLifeSupport().solarRadiationTolerance;
		int damageDealt;
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.ENGINEER)){
				maxPotentialDamage--;
			}
		}
		damageDealt = (int)(Math.random() * maxPotentialDamage);
		Boolean isShipDestroyed = ship.issueHullDamage(damageDealt);
		if (isShipDestroyed) {
			this.eventText = "Your ship took " + damageDealt + " damage from a radiation burst and was destroyed!";
			this.didLose = true;
		} else {
			this.eventText = "Your ship took " + damageDealt + " damaged from a radiation burst!";
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

	private static Boolean isHabitablePlanet(Planet planet, Ship ship) {
		if (planet == null) {
			return Boolean.FALSE;
		}
		int planetGravity = planet.gravity;
		int planetAtmosphericPressure = planet.atmosphericPressure;
		int planetTemperature = planet.temperature;
		AtmosphericComposition planetAir = planet.airType;

		List<CrewMember> crewMembers = ship.listCrewMembers();
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
		List<CrewMember> aliveCrewMembers = ship.listCrewMembers();
		Random rand = new Random();
		int randomCrewMemberIndex = rand.nextInt(aliveCrewMembers.size());
		CrewMember randomCrewMember = aliveCrewMembers.get(randomCrewMemberIndex);
		String crewMemberName = randomCrewMember.name;
		if (randomCrewMember.dealDamage(ambushingSpecies.damage)) {
			ship.removeCrewMember(randomCrewMember);
			this.eventText = "Crew member '" + crewMemberName + "' was killed. ";
		} else {
			this.eventText = "Crew member '" + crewMemberName + "' was injured. ";
		}

		int ammoRequired = (int) (ambushingSpecies.hitPoints / ship.getDamagePerAmmo());
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

	private void handleRecruitmentEvent(Planet planet, Ship ship) {
		List<Species> speciesPresent = planet.speciesPresent;
		Species crewSpecies = ship.getCrewSpecies();
		if (speciesPresent.contains(crewSpecies)) {
			ship.addCrewMember(1);
		}

		this.eventText = "You recruited a new crew member";
	}

	private void handleDesertionEvent(Planet planet, Ship ship) {
		Random rand = new Random();

		List<Species> speciesPresent = planet.speciesPresent;
		Species crewSpecies = ship.getCrewSpecies();

		if (speciesPresent.contains(crewSpecies)) {
			List<CrewMember> aliveCrewMembers = ship.listCrewMembers();
			if (aliveCrewMembers.size() > 2) {
				int randomCrewMemberIndex = rand.nextInt(aliveCrewMembers.size());
				CrewMember randomCrewMember = aliveCrewMembers.get(randomCrewMemberIndex);
				String crewMemberName = randomCrewMember.name;
				ship.removeCrewMember(randomCrewMember);

				this.eventText = "Crew member '" + crewMemberName + "' deserted. ";
			}
		}
	}

	public String getEventText() {
		return this.eventText;
	}

	private void handleRandomEvent(EventContext eventContext, SolarSystem solarSystem, Planet planet, Ship ship) {
		Random rand = new Random();
		int eventKey = rand.nextInt(10);

		if (eventContext == EventContext.PLANET) {
			if (eventKey == 0) {
				int metalAmount = rand.nextInt(5) + 1;
				this.eventText = "You found some ancient ruins. There were" + metalAmount + " units of metal inside";
				ship.addCargo("metals", metalAmount);
			}

			else if (eventKey == 1) {
				int damageAmount = rand.nextInt(10) + 1;
				Boolean isShipDestroyed = ship.issueHullDamage(damageAmount);
				if (isShipDestroyed) {
					this.eventText = "Your ship was destroyed - Game Over";
					this.didLose = Boolean.TRUE;
				}
			}

			else if (eventKey == 2) {
				handleDesertionEvent(planet, ship);
			}

			else if (eventKey == 3) {
				 handleRecruitmentEvent(planet, ship);
			}
		} else if (eventContext == EventContext.SOLAR_SYSTEM) {
			if (eventKey == 0) {
				this.eventText = "There is a solar storm. You must leave this system temporarily.";
			}

			else if (eventKey == 1) {
				int fuelAmount = rand.nextInt(3) + 1;
				this.eventText = "You found an abandoned ship. You were able to salvage" + fuelAmount + " units of fuel from it.";
				ship.addCargo("fuel", fuelAmount);
			}

			else {
				this.eventText = "";
			}
		} else {
			// Galaxy level events
		}
	}
}
