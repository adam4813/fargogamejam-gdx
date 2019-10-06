package fgm.fgj.gamejamgame;

import java.util.List;
import java.util.Random;

public class GameEvent {
	final EventContext eventContext;
	String eventText;
	final String eventType;
	boolean didLose;
	final boolean didWin;
	boolean isPositive;

	public boolean didWin(){
		return this.didWin;
	}

	public boolean didLose(){
		return this.didLose;
	}

	public boolean isPositive(){
		return this.isPositive && this.didWin && !this.didLose;
	}

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
		this.didWin = planet.isHabitablePlanet(ship);
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
		int metalHarvestAmount = rand.nextInt(3);
		int waterHarvestAmount = rand.nextInt(3);
		int fuelHarvestAmount = rand.nextInt(3);

		if (planet.metals > metalHarvestAmount) {
			planet.metals = planet.metals - metalHarvestAmount;
			ship.addCargo("metals", metalHarvestAmount);
		}

		if (planet.water > waterHarvestAmount) {
			planet.water = planet.water - waterHarvestAmount;
			ship.addCargo("water", waterHarvestAmount);
		}

		if (planet.fuel > fuelHarvestAmount) {
			planet.fuel = planet.fuel - fuelHarvestAmount;
			ship.addCargo("fuel", fuelHarvestAmount);
		}

		this.eventText = "You harvested " + metalHarvestAmount + " metals, " + waterHarvestAmount + " water, and " + fuelHarvestAmount + " fuel from the planet.";
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

	private String handleTradeEvent(Ship ship) {
		Random rand = new Random();
		int giveResourceIndex = rand.nextInt(6);
		int giveUnits = rand.nextInt(4) + 1;
		String giveResource;
		switch (giveResourceIndex) {
			case 0: giveResource = "fuel"; ship.getCargoBay().increaseFuel(giveUnits); break;
			case 1: giveResource = "metal"; ship.getCargoBay().increaseMetal(giveUnits); break;
			case 2: giveResource = "water"; ship.getCargoBay().increaseWater(giveUnits); break;
			case 3: giveResource = "gas"; ship.getCargoBay().increaseGas(giveUnits); break;
			case 4: giveResource = "ammo"; ship.getCargoBay().increaseAmmo(giveUnits); break;
			case 5: default: giveResource = "food"; ship.getCargoBay().increaseFood(giveUnits); break;
		}
		int receiveResourceIndex = rand.nextInt(6);
		int receiveUnits = rand.nextInt(4) + 1;
		String receiveResource;
		switch (receiveResourceIndex) {
			case 0: receiveResource = "food"; ship.getCargoBay().decreaseFuel(giveUnits); break;
			case 1: receiveResource = "ammo"; ship.getCargoBay().decreaseMetal(giveUnits); break;
			case 2: receiveResource = "gas"; ship.getCargoBay().decreaseWater(giveUnits); break;
			case 3: receiveResource = "water"; ship.getCargoBay().decreaseGas(giveUnits); break;
			case 4: receiveResource = "metal"; ship.getCargoBay().decreaseAmmo(giveUnits); break;
			case 5: default: receiveResource = "fuel"; ship.getCargoBay().decreaseFood(giveUnits); break;
		}
		return "You gave them " + giveUnits + " " + giveResource + " and received " + receiveUnits + " " + receiveResource + ".";
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
				if (metalAmount == 1) {
					this.eventText = "You found some ancient ruins. There was " + metalAmount + " unit of metal inside";
				} else {
					this.eventText = "You found some ancient ruins. There were " + metalAmount + " units of metal inside";
				}

				ship.addCargo("metals", metalAmount);
			}

			else if (eventKey == 1) {
				int damageAmount = rand.nextInt(10) + 1;
				Boolean isShipDestroyed = ship.issueHullDamage(damageAmount);
				if (isShipDestroyed) {
					this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage and was destroyed.";
					this.didLose = Boolean.TRUE;
				} else {
					this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage.";
				}
			}

			else if (eventKey == 2) {
				handleDesertionEvent(planet, ship);
			}

			else if (eventKey == 3) {
				 handleRecruitmentEvent(planet, ship);
			}

			else if (eventKey == 4) {
				this.eventText = "You traded with the planet '" + planet.getName() + ". " + handleTradeEvent(ship);
			}

			else if (eventKey == 5) {
				List<CrewMember> crewMembers = ship.listCrewMembers();
				int crewMemberIndex = rand.nextInt(crewMembers.size());
				int foodEaten = rand.nextInt(3) + 1;
				this.eventText = "Crew member '" + crewMembers.get(crewMemberIndex).getName() + "' returned from planet '" + planet.getName() + " famished and ate " + foodEaten + " extra food from the cargo bay.";
			}

			else {
				this.eventText = "Nothing happened";
			}
		} else if (eventContext == EventContext.SOLAR_SYSTEM) {
			if (eventKey == 0) {
				this.eventText = "There is a solar storm. You must leave this system temporarily.";
			}

			else if (eventKey == 1) {
				int fuelAmount = rand.nextInt(3) + 1;
				this.eventText = "You found an abandoned coal bunker floating in space. You were able to salvage " + fuelAmount + " units of fuel from it.";
				ship.addCargo("fuel", fuelAmount);
			}

			else if (eventKey == 2) {
				int foodAmount = rand.nextInt(3) + 1;
				this.eventText = "Rats ate " + foodAmount + " units of your food.";
				ship.removeCargo("food", foodAmount);
			}

			else if (eventKey == 3) {
				this.eventText = "You found an abandoned ship adrift in space and ransacked its cargo bay!";
				ship.getCargoBay().increaseMetal((int)(Math.random() * 6));
				ship.getCargoBay().increaseWater((int)(Math.random() * 6));
				ship.getCargoBay().increaseAmmo((int)(Math.random() * 6));
				ship.getCargoBay().increaseFood((int)(Math.random() * 6));
				ship.getCargoBay().increaseFuel((int)(Math.random() * 6));
			}

			else if (eventKey == 4) {
				this.eventText = "You encountered another ship and traded with them. " + handleTradeEvent(ship);
			}

			else {
				this.eventText = "Nothing happened";
			}

		} else {
			this.eventText = "Nothing happened";
		}
	}
}
