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
	boolean didLose;
	boolean isPositive;

	private SolarSystem solarSystem;
	private Planet planet;
	private Ship ship;

	public boolean didLose(){
		return this.didLose;
	}

	public boolean isPositive(){
		return this.isPositive && !this.didLose;
	}

	public GameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		this.eventText = "Nothing happened.";
		this.isPositive = true;
		if (ship == null) {
			throw new IllegalArgumentException("A game event cannot occur with a null ship.");
		}
		this.ship = ship;
		if(solarSystem == null && planet == null){
			this.eventContext = EventContext.GALAXY;
			ship.getCargoBay().decreaseFuel(1);
		}else if(planet == null){
			this.solarSystem = solarSystem;
			this.eventContext = EventContext.SOLAR_SYSTEM;
			if(fuelCost > 0){
				ship.getCargoBay().decreaseFuel(fuelCost);
			}else{
				throw new IllegalArgumentException("Fuel costs to visit another solar system cannot be less than 1.");
			}
		}else{
			this.solarSystem = solarSystem;
			this.planet = planet;
			this.eventContext = EventContext.PLANET;
			ship.getCargoBay().decreaseFuel(1);
		}

		int randomValue = (int)(Math.random() * 100);
		if(planet != null && planet.hasArtifact()){
			handleArtifactEvent();
		}else if(randomValue < solarSystem.pirateThreat) {
			handlePirateEvent();
		}else if(randomValue < solarSystem.debrisRating + solarSystem.pirateThreat) {
			handleDebrisEvent();
		}else if(randomValue < solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			handleRadiationEvent();
		}else if(planet != null && planet.getMostViolentSpecies() != null && randomValue < planet.getMostViolentSpecies().damage + solarSystem.solarRadiation + solarSystem.pirateThreat + solarSystem.debrisRating) {
			if (eventContext == EventContext.PLANET) {
				handleAmbushEvent();
			} else {
				handleRandomEvent(eventContext);
			}
		}else if (randomValue < 70) {
			if (eventContext == EventContext.PLANET) {
				handleResourceCollection();
			} else {
				handleRandomEvent(eventContext);
			}
		}else if (randomValue < 95) {
			handleRandomEvent(eventContext);
		}else {
			this.isPositive = true;
			this.eventText = "Your voyage was uneventful.";
		}
	}

	private void handleRandomEvent(EventContext eventContext) {
		this.eventText = "Nothing happened.";
		this.isPositive = true;
		Random rand = new Random();
		int eventKey = rand.nextInt(6);

		if (eventContext == EventContext.PLANET) {
			switch (eventKey) {
				case 0:
					handleRuinsEvent();
					break;
				case 1:
					handleEarthquakeEvent();
					break;
				case 2:
					handleDesertionEvent();
					break;
				case 3:
					handleRecruitmentEvent();
					break;
				case 4:
					handleTradeEvent();
					break;
				case 5:
					handleExtraEatingEvent();
					break;
				case 6:
					handleAmmoProductEvent();
					break;
			}
		} else if (eventContext == EventContext.SOLAR_SYSTEM) {
			switch (eventKey) {
				case 0:
					handleSolarStormEvent();
					break;
				case 1:
					handleSalvageFuelEvent();
					break;
				case 2:
					handleRatsEvent();
					break;
				case 3:
					handleAbandonedCargoEvent();
					break;
				case 4:
					handleTradeEvent();
					break;
				case 5:
					handleCrewMemberExchange();
					break;
			}
		}
	}

	private void handleArtifactEvent() {
		this.eventText = "You found the rare artifact!";
		this.isPositive = true;
		this.ship.getCargoBay().storeArtifact();
	}

	private void handlePirateEvent() {
		this.isPositive = false;
		int maxResourcesTaken = 7 - this.ship.getEngineSpeed();
		for(CrewMember cm : this.ship.listCrewMembers()){
			if(cm.specialization.equals(PILOT)){
				/* Each pilot increases the maneuverability of the ship. */
				maxResourcesTaken--;
			}
		}
		if(maxResourcesTaken > this.ship.getDamagePerAmmo()){
			int takenMetal = (int)(Math.random() * maxResourcesTaken);
			int takenWater= (int)(Math.random() * maxResourcesTaken);
			int takenAmmo = (int)(Math.random() * maxResourcesTaken);
			int takenFood = (int)(Math.random() * maxResourcesTaken);
			int takenFuel = (int)(Math.random() * maxResourcesTaken);
			this.ship.getCargoBay().decreaseMetal(takenMetal);
			this.ship.getCargoBay().decreaseWater(takenWater);
			this.ship.getCargoBay().decreaseAmmo(takenAmmo);
			this.ship.getCargoBay().decreaseFood(takenFood);
			this.ship.getCargoBay().decreaseFuel(takenFuel);
			this.eventText = "Space pirates overwhelmed your weapons and engines. They've taken " + takenMetal + " metals " + takenWater + " water " + takenAmmo + " ammo " + takenFood + " food " + "and " + takenFuel + " fuel";
		}else{
			this.eventText = "You encountered space pirates and fended them off with your weapons and out maneuvered them before they took anything!";
		}
	}

	private void handleDebrisEvent() {
		this.isPositive = false;
		// If crew has pilot, reduce potential damage amount
		int maxPotentialDamage = 8;
		int damageDealt;
		for(CrewMember cm : this.ship.listCrewMembers()){
			if(cm.specialization.equals(PILOT)){
				maxPotentialDamage--;
			}
		}
		damageDealt = (int)(Math.random() * maxPotentialDamage);
		Boolean isShipDestroyed = this.ship.issueHullDamage(damageDealt);
		if (isShipDestroyed) {
			this.eventText = "Your ship took " + damageDealt + " damage from debris and was destroyed!";
			this.didLose = true;
		} else {
			this.eventText = "Your ship took " + damageDealt + " damage from debris!";
		}
	}

	private void handleRadiationEvent() {
		this.isPositive = false;
		int maxPotentialDamage = this.solarSystem.solarRadiation + 3 - this.ship.getLifeSupport().solarRadiationTolerance;
		int damageDealt;
		for(CrewMember cm : this.ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.ENGINEER)){
				maxPotentialDamage--;
			}
		}
		damageDealt = (int)(Math.random() * maxPotentialDamage);
		Boolean isShipDestroyed = this.ship.issueHullDamage(damageDealt);
		if (isShipDestroyed) {
			this.eventText = "Your ship took " + damageDealt + " damage from a radiation burst and was destroyed!";
			this.didLose = true;
		} else {
			this.eventText = "Your ship took " + damageDealt + " damage from a radiation burst!";
		}
	}

	private void handleResourceCollection() {
		this.isPositive = true;
		int potentialResources = 5;
		for(CrewMember cm : this.ship.listCrewMembers()){
			if(cm.specialization.equals(Specialization.SCIENTIST)){
				potentialResources++;
			}
		}
		int metalHarvestAmount = (int)(Math.random() * potentialResources);
		int waterHarvestAmount = (int)(Math.random() * potentialResources);
		int fuelHarvestAmount = (int)(Math.random() * potentialResources);
		this.planet.metals -= metalHarvestAmount;
		if(this.planet.metals < 0){
			/* They've collected the last of the metals. */
			metalHarvestAmount -= this.planet.metals;
			this.planet.metals = 0;
		}
		ship.getCargoBay().increaseMetal(metalHarvestAmount);

		this.planet.water -= waterHarvestAmount;
		if(this.planet.water < 0){
			/* They've collected the last of the water. */
			waterHarvestAmount -= this.planet.water;
			this.planet.water = 0;
		}
		ship.getCargoBay().increaseWater(waterHarvestAmount);

		this.planet.fuel -= fuelHarvestAmount;
		if(this.planet.fuel < 0){
			/* They've collected the last of the fuel. */
			fuelHarvestAmount -= this.planet.fuel;
			this.planet.fuel = 0;
		}
		this.ship.getCargoBay().increaseFuel(waterHarvestAmount);
		this.eventText = "You collected " + metalHarvestAmount + " metals, " + waterHarvestAmount + " water, and " + fuelHarvestAmount + " fuel from the planet.";
	}

	private void handleAmbushEvent() {
		this.isPositive = false;
		Species ambushingSpecies = this.planet.getMostViolentSpecies();
		// Get random crew member to the ambush will injure or kill
		List<CrewMember> aliveCrewMembers = this.ship.listCrewMembers();
		Random rand = new Random();
		int randomCrewMemberIndex = rand.nextInt(aliveCrewMembers.size());
		CrewMember randomCrewMember = aliveCrewMembers.get(randomCrewMemberIndex);
		String crewMemberName = randomCrewMember.name;
		if (randomCrewMember.dealDamage(ambushingSpecies.damage)) {
			this.ship.removeCrewMember(randomCrewMember);
			this.eventText = "Crew member '" + crewMemberName + "' was killed. ";
		} else {
			this.eventText = "Crew member '" + crewMemberName + "' was injured. ";
		}

		int ammoRequired = (int) (ambushingSpecies.hitPoints / this.ship.getDamagePerAmmo());
		int currentAmmo = this.ship.getCargoBay().checkAmmo();
		if (currentAmmo < ammoRequired) {
			this.eventText += "Ran out of ammo, no food acquired. ";
		} else {
			if (currentAmmo == ammoRequired) {
				this.eventText += "Ran out of ammo. ";
			}
			this.ship.getCargoBay().increaseFood(ambushingSpecies.mass);
			this.eventText += "Acquired " + ambushingSpecies.mass + " food.";
		}
		this.ship.getCargoBay().decreaseAmmo(ammoRequired);
	}

	private void handleRecruitmentEvent() {
		List<Species> speciesPresent = this.planet.speciesPresent;
		Species crewSpecies = this.ship.getCrewSpecies();
		if (speciesPresent.contains(crewSpecies)) {
			this.ship.addCrewMember(1);
		}
		this.eventText = "You recruited a new crew member";
		this.isPositive = true;
	}

	private void handleDesertionEvent() {
		Random rand = new Random();

		List<Species> speciesPresent = this.planet.speciesPresent;
		Species crewSpecies = this.ship.getCrewSpecies();

		if (speciesPresent.contains(crewSpecies)) {
			List<CrewMember> aliveCrewMembers = this.ship.listCrewMembers();
			if (aliveCrewMembers.size() > 2) {
				int randomCrewMemberIndex = rand.nextInt(aliveCrewMembers.size());
				CrewMember randomCrewMember = aliveCrewMembers.get(randomCrewMemberIndex);
				String crewMemberName = randomCrewMember.name;
				this.ship.removeCrewMember(randomCrewMember);

				this.eventText = "Crew member '" + crewMemberName + "' deserted. ";
				return;
			}
		}
		this.eventText = "Crew members seem happy.";
		this.isPositive = false;
	}

	private void handleTradeEvent() {
		Random rand = new Random();
		int giveResourceIndex = rand.nextInt(6);
		int giveUnits = rand.nextInt(4) + 1;
		String giveResource;
		switch (giveResourceIndex) {
			case 0: giveResource = "fuel"; this.ship.getCargoBay().increaseFuel(giveUnits); break;
			case 1: giveResource = "metal"; this.ship.getCargoBay().increaseMetal(giveUnits); break;
			case 2: giveResource = "water"; this.ship.getCargoBay().increaseWater(giveUnits); break;
			case 3: giveResource = "gas"; this.ship.getCargoBay().increaseGas(giveUnits); break;
			case 4: giveResource = "ammo"; this.ship.getCargoBay().increaseAmmo(giveUnits); break;
			case 5: default: giveResource = "food"; this.ship.getCargoBay().increaseFood(giveUnits); break;
		}
		int receiveResourceIndex = rand.nextInt(6);
		int receiveUnits = rand.nextInt(4) + 1;
		String receiveResource;
		switch (receiveResourceIndex) {
			case 0: receiveResource = "food"; this.ship.getCargoBay().decreaseFuel(giveUnits); break;
			case 1: receiveResource = "ammo"; this.ship.getCargoBay().decreaseMetal(giveUnits); break;
			case 2: receiveResource = "gas"; this.ship.getCargoBay().decreaseWater(giveUnits); break;
			case 3: receiveResource = "water"; this.ship.getCargoBay().decreaseGas(giveUnits); break;
			case 4: receiveResource = "metal"; this.ship.getCargoBay().decreaseAmmo(giveUnits); break;
			case 5: default: receiveResource = "fuel"; this.ship.getCargoBay().decreaseFood(giveUnits); break;
		}
		if (this.planet != null) {
			this.eventText = "You traded with the planet '" + this.planet.getName() + ".' ";
		} else {
			this.eventText = "You encountered another ship and traded with them. ";
		}
		this.eventText += "You gave them " + giveUnits + " " + giveResource + " and received " + receiveUnits + " " + receiveResource + ".";
		this.isPositive = true;
	}

	private void handleCrewMemberExchange() {
		List<CrewMember> crewMembers = this.ship.listCrewMembers();
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
				this.ship.addCrewMember(new CrewMember(tradeToName, engineers.get(1).species, PILOT, 0));
				this.ship.removeCrewMember(engineers.get(1));
			} else if (scientists.size() == 0) {
				tradeAwayName = engineers.get(1).name;
				tradeAwaySpecialization = "engineer";
				tradeToSpecialization = "scientist";
				this.ship.addCrewMember(new CrewMember(tradeToName, engineers.get(1).species, SCIENTIST, 0));
				this.ship.removeCrewMember(engineers.get(1));
			}
		} else if (pilots.size() > 1) {
			if (engineers.size() == 0) {
				tradeAwayName = pilots.get(1).name;
				tradeAwaySpecialization = "pilot";
				tradeToSpecialization = "engineer";
				this.ship.addCrewMember(new CrewMember(tradeToName, pilots.get(1).species, ENGINEER, 0));
				this.ship.removeCrewMember(pilots.get(1));
			} else if (scientists.size() == 0) {
				tradeAwayName = pilots.get(1).name;
				tradeAwaySpecialization = "pilot";
				tradeToSpecialization = "scientist";
				this.ship.addCrewMember(new CrewMember(tradeToName, pilots.get(1).species, SCIENTIST, 0));
				this.ship.removeCrewMember(pilots.get(1));
			}
		} else if (scientists.size() > 1) {
			if (engineers.size() == 0) {
				tradeAwayName = scientists.get(1).name;
				tradeAwaySpecialization = "scientist";
				tradeToSpecialization = "engineer";
				this.ship.addCrewMember(new CrewMember(tradeToName, scientists.get(1).species, ENGINEER, 0));
				this.ship.removeCrewMember(scientists.get(1));
			} else if (pilots.size() == 0) {
				tradeAwayName = scientists.get(1).name;
				tradeAwaySpecialization = "scientist";
				tradeToSpecialization = "pilot";
				this.ship.addCrewMember(new CrewMember(tradeToName, scientists.get(1).species, PILOT, 0));
				this.ship.removeCrewMember(scientists.get(1));
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

	public void handleAmmoProductEvent() {
		Random rand = new Random();
		int metals = this.ship.getCargoBay().checkMetal();
		if (metals == 0) {
			this.eventText = "You tried to trade metal for ammo, but you didn't have any metal.";
		} else {
			int metalToTrade = rand.nextInt(metals) + 1;
			for(CrewMember cm : this.ship.listCrewMembers()){
				if(cm.specialization.equals(Specialization.SCIENTIST)){
					metalToTrade++;
				}
			}
			int addedAmmo = metalToTrade * 2;
			this.ship.getCargoBay().decreaseMetal(metalToTrade);
			this.ship.getCargoBay().increaseAmmo(addedAmmo);
			this.eventText = "You traded " + metalToTrade + " metal for " + addedAmmo + " ammo.";
		}
	}

	private void handleRuinsEvent() {
		int metalAmount = new Random().nextInt(5) + 1;
		if (metalAmount == 1) {
			this.eventText = "You found some ancient ruins. There was " + metalAmount + " unit of metal inside";
		} else {
			this.eventText = "You found some ancient ruins. There were " + metalAmount + " units of metal inside";
		}
		this.isPositive = true;
		this.ship.getCargoBay().increaseMetal(metalAmount);
	}

	private void handleEarthquakeEvent() {
		int damageAmount = new Random().nextInt(10) + 1;
		Boolean isShipDestroyed = this.ship.issueHullDamage(damageAmount);
		this.isPositive = false;
		if (isShipDestroyed) {
			this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage and was destroyed.";
			this.didLose = Boolean.TRUE;
		} else {
			this.eventText = "An earthquake occurred. Your ship took" + damageAmount + " damage.";
		}
	}

	private void handleExtraEatingEvent() {
		Random rand = new Random();
		List<CrewMember> crewMembers = this.ship.listCrewMembers();
		int crewMemberIndex = rand.nextInt(crewMembers.size());
		int foodEaten = rand.nextInt(3) + 1;
		this.eventText = "Crew member '" + crewMembers.get(crewMemberIndex).getName() + "' returned from planet '" + this.planet.getName() + "' famished and ate " + foodEaten + " extra food from the cargo bay.";
		this.ship.getCargoBay().decreaseFood(foodEaten);
		this.isPositive = false;
	}

	private void handleSolarStormEvent() {
		this.eventText = "There is a solar storm. You must leave this system temporarily.";
		this.isPositive = false;
	}

	private void handleSalvageFuelEvent() {
		int fuelAmount = new Random().nextInt(3) + 1;
		this.eventText = "You found an abandoned coal bunker floating in space. You were able to salvage " + fuelAmount + " units of fuel from it.";
		this.ship.getCargoBay().increaseFuel(fuelAmount);
		this.isPositive = true;
	}

	private void handleRatsEvent() {
		int foodAmount = new Random().nextInt(3) + 1;
		this.eventText = "Rats ate " + foodAmount + " units of your food.";
		this.ship.getCargoBay().decreaseFood(foodAmount);
		this.isPositive = false;
	}

	private void handleAbandonedCargoEvent() {
		this.eventText = "You found an abandoned ship adrift in space and ransacked its cargo bay!";
		this.ship.getCargoBay().increaseMetal((int)(Math.random() * 6));
		this.ship.getCargoBay().increaseWater((int)(Math.random() * 6));
		this.ship.getCargoBay().increaseAmmo((int)(Math.random() * 6));
		this.ship.getCargoBay().increaseFood((int)(Math.random() * 6));
		this.ship.getCargoBay().increaseFuel((int)(Math.random() * 6));
		this.isPositive = true;
	}

	public String getEventText() {
		return this.eventText;
	}

}
