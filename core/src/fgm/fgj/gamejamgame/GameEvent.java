package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fgm.fgj.gamejamgame.Specialization.ENGINEER;
import static fgm.fgj.gamejamgame.Specialization.PILOT;
import static fgm.fgj.gamejamgame.Specialization.SCIENTIST;

public class GameEvent {
	final EventContext eventContext;
	String eventText;
	final String eventType;
	boolean didLose;
	boolean isPositive;

	public boolean didLose(){
		return this.didLose;
	}

	public boolean isPositive(){
		return this.isPositive && !this.didLose;
	}

	public GameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		if (ship == null) {
			throw new IllegalArgumentException("A game event cannot occur with a null ship.");
		}
		if(solarSystem == null && planet == null){
			this.eventContext = EventContext.GALAXY;
			ship.getCargoBay().decreaseFuel(1);
		}else if(planet == null){
			this.eventContext = EventContext.SOLAR_SYSTEM;
			if(fuelCost > 0){
				ship.getCargoBay().decreaseFuel(fuelCost);
			}else{
				throw new IllegalArgumentException("Fuel costs to visit another solar system cannot be less than 1.");
			}
		}else{
			this.eventContext = EventContext.PLANET;
			ship.getCargoBay().decreaseFuel(1);
		}
		int randomValue = (int)(Math.random() * 100);
		if(planet != null && planet.hasArtifact()){
			this.eventType = "artifact";
			this.isPositive = true;
		}else if(randomValue < solarSystem.pirateThreat) {
			this.eventType = "pirate";
			this.isPositive = false;
		}else if(randomValue < solarSystem.debrisRating + solarSystem.pirateThreat) {
			this.eventType = "debris";
			this.isPositive = false;
		}else if(randomValue < solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "radiation";
			this.isPositive = false;
		}else if(planet != null && planet.getMostViolentSpecies() != null && randomValue < planet.getMostViolentSpecies().damage + solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			this.eventType = "ambush";
			this.isPositive = false;
		}else if (randomValue < 70) {
			this.eventType = "resource";
			this.isPositive = true;
		}else if (randomValue < 95) {
			this.eventType = "other";
			this.isPositive = false;
		}else {
			this.eventType = "none";
			this.isPositive = true;
		}
		handleGameEvent(this.eventType, this.eventContext, solarSystem, planet, ship);
	}

	private void handleGameEvent(String eventType, EventContext eventContext, SolarSystem solarSystem, Planet planet, Ship ship) {
		switch(eventType) {
			case "artifact":
				handleArtifactEvent(ship);
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

	private void handleArtifactEvent(Ship ship) {
		this.eventText = "You found the rare artifact!";
		this.isPositive = true;
		ship.getCargoBay().storeArtifact();
	}

	private void handlePirateEvent(Ship ship) {
		Random rand = new Random();
		int maxResourcesTaken = 7 - ship.getEngineSpeed();
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(PILOT)){
				/* Each pilot increases the maneuverability of the ship. */
				maxResourcesTaken--;
			}
		}
		if(maxResourcesTaken > ship.getDamagePerAmmo()){
			int takenMetal = (int)(Math.random() * maxResourcesTaken);
			int takenWater= (int)(Math.random() * maxResourcesTaken);
			int takenAmmo = (int)(Math.random() * maxResourcesTaken);
			int takenFood = (int)(Math.random() * maxResourcesTaken);
			int takenFuel = (int)(Math.random() * maxResourcesTaken);
			ship.getCargoBay().decreaseMetal(takenMetal);
			ship.getCargoBay().decreaseWater(takenWater);
			ship.getCargoBay().decreaseAmmo(takenAmmo);
			ship.getCargoBay().decreaseFood(takenFood);
			ship.getCargoBay().decreaseFuel(takenFuel);
			this.eventText = "Space pirates overwhelmed your weapons and engines. They've taken " + takenMetal + " metals " + takenWater + " water " + takenAmmo + " ammo " + takenFood + " food " + "and " + takenFuel + " fuel";
		}else{
			this.eventText = "You encountered space pirates and fended them off with your weapons and out maneuvered them before they took anything!";
		}
	}

	private void handleDebrisEvent(Ship ship) {
		// If crew has pilot, reduce potential damage amount
		int maxPotentialDamage = 8;
		int damageDealt;
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(PILOT)){
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
			this.eventText = "Your ship took " + damageDealt + " damage from a radiation burst!";
		}
	}

	private void handleResourceCollection(Planet planet, Ship ship) {
		int potentialResources = 5;
		for(CrewMember cm : ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.SCIENTIST)){
				potentialResources++;
			}
		}
		int metalHarvestAmount = (int)(Math.random() * potentialResources);
		int waterHarvestAmount = (int)(Math.random() * potentialResources);
		int fuelHarvestAmount = (int)(Math.random() * potentialResources);
		planet.metals -= metalHarvestAmount;
		if(planet.metals < 0){
			/* They've collected the last of the metals. */
			metalHarvestAmount -= planet.metals;
			planet.metals = 0;
		}
		ship.getCargoBay().increaseMetal(metalHarvestAmount);

		planet.water -= waterHarvestAmount;
		if(planet.water < 0){
			/* They've collected the last of the water. */
			waterHarvestAmount -= planet.water;
			planet.water = 0;
		}
		ship.getCargoBay().increaseWater(waterHarvestAmount);

		planet.fuel -= fuelHarvestAmount;
		if(planet.fuel < 0){
			/* They've collected the last of the fuel. */
			fuelHarvestAmount -= planet.fuel;
			planet.fuel = 0;
		}
		ship.getCargoBay().increaseFuel(waterHarvestAmount);
		this.eventText = "You collected " + metalHarvestAmount + " metals, " + waterHarvestAmount + " water, and " + fuelHarvestAmount + " fuel from the planet.";
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
				return;
			}
		}
		this.eventText = "Crew members seem happy.";
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

	private void handleCrewMemberExchange(Ship ship) {
		List<CrewMember> crewMembers = ship.listCrewMembers();
		List<CrewMember> engineers = new ArrayList<>();
		List<CrewMember> pilots = new ArrayList<>();
		List<CrewMember> scientists = new ArrayList<>();
		for (CrewMember crewMember: crewMembers) {
			switch (crewMember.specialization) {
				case ENGINEER: engineers.add(crewMember); break;
				case PILOT: pilots.add(crewMember); break;
				case SCIENTIST: scientists.add(crewMember); break;
			}
		}
		String tradeAwayName = "";
		String tradeAwaySpecialization = "";
		String tradeToSpecialization = "";
		String tradeToName = Galaxy.generateCrewName();
		if (engineers.size() > 1) {
			if (pilots.size() == 0) {
				tradeAwayName = engineers.get(1).name;
				tradeAwaySpecialization = "engineer";
				tradeToSpecialization = "pilot";
				ship.addCrewMember(new CrewMember(tradeToName, engineers.get(1).species, PILOT, 0));
				ship.removeCrewMember(engineers.get(1));
			} else if (scientists.size() == 0) {
				tradeAwayName = engineers.get(1).name;
				tradeAwaySpecialization = "engineer";
				tradeToSpecialization = "scientist";
				ship.addCrewMember(new CrewMember(tradeToName, engineers.get(1).species, SCIENTIST, 0));
				ship.removeCrewMember(engineers.get(1));
			}
		} else if (pilots.size() > 1) {
			if (engineers.size() == 0) {
				tradeAwayName = pilots.get(1).name;
				tradeAwaySpecialization = "pilot";
				tradeToSpecialization = "engineer";
				ship.addCrewMember(new CrewMember(tradeToName, pilots.get(1).species, ENGINEER, 0));
				ship.removeCrewMember(pilots.get(1));
			} else if (scientists.size() == 0) {
				tradeAwayName = pilots.get(1).name;
				tradeAwaySpecialization = "pilot";
				tradeToSpecialization = "scientist";
				ship.addCrewMember(new CrewMember(tradeToName, pilots.get(1).species, SCIENTIST, 0));
				ship.removeCrewMember(pilots.get(1));
			}
		} else if (scientists.size() > 1) {
			if (engineers.size() == 0) {
				tradeAwayName = scientists.get(1).name;
				tradeAwaySpecialization = "scientist";
				tradeToSpecialization = "engineer";
				ship.addCrewMember(new CrewMember(tradeToName, scientists.get(1).species, ENGINEER, 0));
				ship.removeCrewMember(scientists.get(1));
			} else if (pilots.size() == 0) {
				tradeAwayName = scientists.get(1).name;
				tradeAwaySpecialization = "scientist";
				tradeToSpecialization = "pilot";
				ship.addCrewMember(new CrewMember(tradeToName, scientists.get(1).species, PILOT, 0));
				ship.removeCrewMember(scientists.get(1));
			}
		}
		if (tradeAwayName != "" && tradeToName != "" && tradeToSpecialization != "" && tradeAwaySpecialization != "") {
			this.eventText = "A passing ship noticed you had no " + tradeToSpecialization
				+ ", so they offered a crew member exchange and gave you '" + tradeToName
				+ "' in exchange for your extra " + tradeAwaySpecialization + " named '" + tradeAwayName + ".'";
		} else {
			this.eventText = "You tried to initiate a crew member exchange, but it didn't work out.";
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
				if (metalAmount == 1) {
					this.eventText = "You found some ancient ruins. There was " + metalAmount + " unit of metal inside";
				} else {
					this.eventText = "You found some ancient ruins. There were " + metalAmount + " units of metal inside";
				}
				this.isPositive = true;
				ship.getCargoBay().increaseMetal(metalAmount);
			}

			else if (eventKey == 1) {
				int damageAmount = rand.nextInt(10) + 1;
				Boolean isShipDestroyed = ship.issueHullDamage(damageAmount);
				this.isPositive = false;
				if (isShipDestroyed) {
					this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage and was destroyed.";
					this.didLose = Boolean.TRUE;
				} else {
					this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage.";
				}
			}

			else if (eventKey == 2) {
				handleDesertionEvent(planet, ship);
				this.isPositive = false;
			}

			else if (eventKey == 3) {
				 handleRecruitmentEvent(planet, ship);
				this.isPositive = true;
			}

			else if (eventKey == 4) {
				this.eventText = "You traded with the planet '" + planet.getName() + ". " + handleTradeEvent(ship);
				this.isPositive = true;
			}

			else if (eventKey == 5) {
				List<CrewMember> crewMembers = ship.listCrewMembers();
				int crewMemberIndex = rand.nextInt(crewMembers.size());
				int foodEaten = rand.nextInt(3) + 1;
				this.eventText = "Crew member '" + crewMembers.get(crewMemberIndex).getName() + "' returned from planet '" + planet.getName() + " famished and ate " + foodEaten + " extra food from the cargo bay.";
				this.isPositive = true;
			}

			else {
				this.eventText = "Nothing happened";
				this.isPositive = true;
			}
		} else if (eventContext == EventContext.SOLAR_SYSTEM) {
			if (eventKey == 0) {
				this.eventText = "There is a solar storm. You must leave this system temporarily.";
				this.isPositive = false;
			}

			else if (eventKey == 1) {
				int fuelAmount = rand.nextInt(3) + 1;
				this.eventText = "You found an abandoned coal bunker floating in space. You were able to salvage " + fuelAmount + " units of fuel from it.";
				ship.getCargoBay().increaseFuel(fuelAmount);
				this.isPositive = true;
			}

			else if (eventKey == 2) {
				int foodAmount = rand.nextInt(3) + 1;
				this.eventText = "Rats ate " + foodAmount + " units of your food.";
				ship.getCargoBay().decreaseFood(foodAmount);
				this.isPositive = false;
			}

			else if (eventKey == 3) {
				this.eventText = "You found an abandoned ship adrift in space and ransacked its cargo bay!";
				ship.getCargoBay().increaseMetal((int)(Math.random() * 6));
				ship.getCargoBay().increaseWater((int)(Math.random() * 6));
				ship.getCargoBay().increaseAmmo((int)(Math.random() * 6));
				ship.getCargoBay().increaseFood((int)(Math.random() * 6));
				ship.getCargoBay().increaseFuel((int)(Math.random() * 6));
				this.isPositive = true;
			}

			else if (eventKey == 4) {
				this.eventText = "You encountered another ship and traded with them. " + handleTradeEvent(ship);
				this.isPositive = true;
			}

			else if (eventKey == 5) {
				handleCrewMemberExchange(ship);
				this.isPositive = true;
			}

			else {
				this.eventText = "Nothing happened";
				this.isPositive = true;
			}

		} else {
			this.eventText = "Nothing happened";
			this.isPositive = true;
		}
	}
}
