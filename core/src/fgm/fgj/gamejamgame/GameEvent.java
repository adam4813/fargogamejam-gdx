package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents how the game state is manipulated. */
public class GameEvent {
	/** Represents the kinds of events that may occur. */
	private enum Context{
		NO_CREW_REMAINING(false, true, "There isn't a crew remaining to operate the ship!", "Our journey has come to an unfortunate end."),
		SHIP_DESTROYED(false, true, "Our ship has been destroyed!", "Our journey has come to an unfortunate end."),
		NO_GAS_REMAINING(false, true, "Our crew had insufficient gas to breathe!", "Our journey has come to an unfortunate end."),
		NO_FUEL_REMAINING(false, true, "Our ship ran out of fuel!", "Our journey has come to an unfortunate end."),
		FOUND_HABITABLE_PLANET(true, false, "This planet... is a lot like our home!", "Our journey has come to an end, we've found a new home!"/*, "Eh, there might be better homes."*/),
		FOUND_ARTIFACT(false, false, "Our crew has found a mysterious artifact!", "Take the artifact."),
		AMBUSH(false, false, "Something is engaging us!", "Fight them!"),
		GATHER_PLANET_RESOURCES(false, false, "Our crew has detected resources!", "Have our scientists gather what they can."/*, "Have our scientists focus on fuels", "Have our scientists focus on metals", "Have our scientists focus on water"*/),
		PIRATE_ATTACK(false, false, "Pirates are engaging us!", "Fight them!"/*, "Try to escape."*/),
		DEBRIS_STRIKE(false, false, "Debris is on a collision trajectory with us!","Maneuver away."),
		RADIATION_BURST(false, false, "Our crew is suffering in a strong radiation field!", "Scramble your engineers to bolster your life support systems."),
		PEACEFUL(false, false, "It's quiet...", "Just how I like it.", "Too quiet...");
		/** Represents the text displayed in the UI for context so the player can make an informed decision on their provided options, cannot be null or "". */
		private final String eventText;
		/** Represents the text displayed in the UI to indicate the options the player may choose, which change how the event is executed. Cannot be null or empty. */
		private final List<String> options;
		/** Represents, when true, that the UI should display the lose screen. */
		private final boolean isLosingContext;
		/** Represents, when true, that the UI should display the win screen. */
		private final boolean isWinningContext;

		/** Instantiates event context with the given parameters.
		 * @param isWinningContext {@link Context#isWinningContext}
		 * @param isLosingContext {@link Context#isLosingContext}
		 * @param eventText {@link Context#eventText}
		 * @param options {@link Context#options}
		 */
		Context(final boolean isWinningContext, final boolean isLosingContext, final String eventText, String ...options){
			if(eventText == null){
				throw new IllegalArgumentException("A game event's context text cannot be null.");
			}
			if(eventText.equals("")){
				throw new IllegalArgumentException("A game event's context text cannot be empty.");
			}
			if(options.length < 1){
				throw new IllegalArgumentException("A game event's context must have at least 1 option.");
			}
			this.eventText = eventText;
			this.options = new ArrayList<>();
			this.isWinningContext = isWinningContext;
			this.isLosingContext = isLosingContext;
			for(String option : options){
				if(option == null){
					throw new IllegalArgumentException("A game event's context option cannot be null.");
				}
				if(option.equals("")){
					throw new IllegalArgumentException("A game event's context option cannot be empty.");
				}
				this.options.add(option);
			}
		}

		/** @return {@link Context#isWinningContext} */
		boolean isWinningContext(){
			return this.isWinningContext;
		}

		/** @return {@link Context#isLosingContext} */
		boolean isLosingContext(){
			return this.isLosingContext;
		}

		/** @return {@link Context#eventText} */
		String getEventText(){
			return this.eventText;
		}

		/** @return an unmodifiable list for {@link Context#options} */
		List<String> getOptions(){
			return Collections.unmodifiableList(this.options);
		}
	}

	/** Cannot be null.
	 * @see Context
	 */
	private final Context context;
	/** Represents the text displayed in the UI after an option was executed. */
	private String resultText;
	/** Represents a reference to execute on the same ship used to create the event, cannot be null.
	 * Note that the game isn't thread-safe so the original reference may be modified between the game event's instantiation and execution.
	 * Due to the nature of the game it is safe to assume they are in the same state though.
	 * @see Ship
	 */
	private final Ship ship;
	/** Represents a reference to execute on the same solarSystem system used to create the event.
	 * Note that the game isn't thread-safe so the original reference may be modified between the game event's instantiation and execution.
	 * Due to the nature of the game it is safe to assume they are in the same state though.
	 * @see SolarSystem
	 */
	private final SolarSystem solarSystem;
	/** Represents a reference to execute on the same planet used to create the event.
	 * Note that the game isn't thread-safe so the original reference may be modified between the game event's instantiation and execution.
	 * Due to the nature of the game it is safe to assume they are in the same state though.
	 * @see Planet
	 */
	private final Planet planet;
	/** Represents, when true, that the event has been executed and shouldn't be used again. This is to prevent accidental repeats of events and potential cheaters from using the event more than once. */
	private boolean executed;

	/** Instantiates a new game event based on the game state provided.
	 * @param solarSystem represents the solarSystem system the player resides in for event generation. May be manipulated to resolve the event.
	 * @param fuelCost represents the cost of traveling that generated the event.
	 * @param planet represents the planet the player resides in for event generation. May be manipulated to resolve the event.
	 * @param ship represents the player for event generation. May be manipulated to resolve the event.
	 */
	GameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		/* TODO instantiate the event context but do not carry out its effect. */
		/* Cargo errors include using the wrong resource type (-1) or overflows and underflows of values greater than 0.
		* Underflows will often be game losing. */
		int cargoError;
		if (ship == null) {
			throw new IllegalArgumentException("A game event cannot occur with a null ship.");
		}
		this.executed = false;
		this.solarSystem = solarSystem;
		this.planet = planet;
		this.ship = ship;
		this.resultText = "Choose an option.";
		/* Each crew member consumes 1 gas, but is reduced by life support. */
		cargoError = ship.removeCargo(ResourceTypes.GAS, ship.getCrewMembers().size() - ship.getLifeSupportGasEfficiency());
		if(cargoError > 0){
			/* The player loses if they don't have enough gas to sustain their crew. */
			this.context = Context.NO_GAS_REMAINING;
			return;
		}
		/* Prioritize losing events. */
		if(ship.getCrewMembers().isEmpty()){
			this.context = Context.NO_CREW_REMAINING;
			return;
		}
		if(ship.damageEngine(0)){
			this.context = Context.SHIP_DESTROYED;
			return;
		}
		if(solarSystem != null && planet != null){
			/* TODO prioritize 'planet' only events */
			ship.setPirateThreat(+1);
			int pirateThreat = ship.getPirateThreat() + solarSystem.pirateThreat;
			Species mostViolent = planet.getMostViolentSpecies();
			int roll = (int)(Math.random() * 60);
			/* Visiting planets in a solarSystem system always take 1 fuel. */
			cargoError = ship.removeCargo(ResourceTypes.FUEL, 1);
			if(cargoError > 0){
				this.context = Context.NO_FUEL_REMAINING;
				return;
			}
			if(planet.checkResource(ResourceTypes.ARTIFACT) != 0){
				this.context = Context.FOUND_ARTIFACT;
				return;
			}
			if(planet.isHabitablePlanet(ship)){
				this.context = Context.FOUND_HABITABLE_PLANET;
				return;
			}
			if(roll < ((mostViolent != null) ? mostViolent.damage * 3 : 0)){
				this.context = Context.AMBUSH;
				return;
			}else if(roll < ((mostViolent != null) ? mostViolent.damage * 3 : 0) + pirateThreat){
				this.context = Context.PIRATE_ATTACK;
				return;
			}else{
				this.context = Context.GATHER_PLANET_RESOURCES;
				return;
			}
		}else if(solarSystem != null){
			/* TODO prioritize 'solarSystem system' only events */
			int roll = (int)(Math.random() * 60);
			ship.setPirateThreat(-ship.getEngineSpeed());
			int pirateThreat = ship.getPirateThreat() + solarSystem.pirateThreat;
			cargoError = ship.removeCargo(ResourceTypes.FUEL, fuelCost - ship.getEngineEfficiency());
			if(cargoError > 0){
				this.context = Context.NO_FUEL_REMAINING;
				return;
			}
			if(roll < pirateThreat ){
				this.context = Context.PIRATE_ATTACK;
				return;
			}else if(roll < solarSystem.debrisRating + pirateThreat){
				this.context = Context.DEBRIS_STRIKE;
				return;
			}else if(roll < solarSystem.solarRadiation + solarSystem.debrisRating + pirateThreat){
				this.context = Context.RADIATION_BURST;
				return;
			}
		}else{
			/* TODO prioritize 'galaxy' only events */
			cargoError = ship.removeCargo(ResourceTypes.FUEL, fuelCost - ship.getEngineEfficiency());
			if(cargoError > 0){
				this.context = Context.NO_FUEL_REMAINING;
				return;
			}
		}
		/* TODO prioritze anytime events. */
		this.context = Context.PEACEFUL;
	}

	/** @return {@link Context#isLosingContext()} */
	public boolean didLose(){
		return this.context.isLosingContext();
	}

	/** @return {@link Context#isWinningContext()} */
	public boolean didWin(){
		return this.context.isWinningContext();
	}

	/** Executes the event using the option provided if it hasn't been executed before..
	 * @param index represents the option used to execute the event.
	 * @see GameEvent#executed
	 */
	public void execute(int index){
		if(!this.executed){
			this.executed = true;
			this.resultText = "Error. GameEvent Context is not recognized.";
			switch(this.context){
				case NO_CREW_REMAINING:
				case NO_GAS_REMAINING:
				case NO_FUEL_REMAINING:
				case SHIP_DESTROYED:
					/* TODO Trigger/Signal the end of the game so that the UI displays the lose screen. */
					break;
				case FOUND_HABITABLE_PLANET:
					/* TODO Trigger/Signal the end of the game so that the UI displays the win screen. */
					break;
				case FOUND_ARTIFACT:
					this.executeFoundArtifact(index);
					break;
				case AMBUSH:
					this.executeAmbush(index);
					break;
				case GATHER_PLANET_RESOURCES:
					this.executeGatherPlanetResources(index);
					break;
				case PIRATE_ATTACK:
					this.executePirateAttack(index);
					break;
				case DEBRIS_STRIKE:
					this.executeDebrisStrike(index);
					break;
				case RADIATION_BURST:
					this.executeRadiationBurst(index);
					break;
				case PEACEFUL:
					this.executePeaceful(index);
					break;
				default:
					/* The GameEvent's context isn't recognized. */
					break;
			}
		}
	}

	/** @return {@link Context#getEventText()} */
	public String getEventText() {
		return this.context.getEventText();
	}

	/** @return {@link Context#getOptions()} */
	public List<String> getOptions(){
		return this.context.getOptions();
	}

	/** @return {@link GameEvent#resultText} */
	public String getResultText(){
		return this.resultText;
	}

	/** Removes the artifact from the planet and puts it into cargo.
	 * @param index is ignored due to there being one outcome. */
	private void executeFoundArtifact(int index) {
		/* TODO consider:
		 * Allowing the artifact to 'warp' the ship to a new galaxy.
		 * It could be traded for a level 5 module.
		 * Unlocks a secret ending when the game ends with the artifact in cargo.
		 * Provides an extra life when a game losing condition is met while the artifact is present in inventory. */
		switch (index){
			case 0:
			default:
				int cargoError = ship.addCargo(ResourceTypes.ARTIFACT, 1);
				if(cargoError > 0){
					/* The cargo already had a artifact. */
					this.resultText = "You cannot carry two artifacts.";
					break;
				}
				cargoError = planet.depleteResource(ResourceTypes.ARTIFACT, 1);
				if(cargoError > 0){
					/* The planet doesn't have the artifact. This would be caused by a threading issue or events occurring before this was executed. */
					this.resultText = "The planet no longer has an artifact!?";
					break;
				}
				this.resultText = "You've collected the artifact!";
				break;
		}
	}

	/** A crew member is attacked and must defend himself.
	 * @param index is ignored due to there being one outcome.
	 */
	private void executeAmbush(int index) {
		switch(index){
			case 0:
			default:
				int cargoError = 0;
				CrewMember ambushedCrewMember = this.ship.getCrewMembers().get((int)(Math.random() * this.ship.getCrewMembers().size()));
				Species ambushingSpecies = this.planet.getMostViolentSpecies();
				this.resultText = ambushedCrewMember.getName() + " is ambushed!";
				/* Pit the two in mortal combat until one dies. */
				int ambushingSpeciesDamageTaken = 0;
				while(ambushingSpeciesDamageTaken < ambushingSpecies.hitPoints && !ambushedCrewMember.damage(ambushingSpecies.damage)){
					this.resultText += "\n" + ambushedCrewMember.getName() + " takes " + ambushingSpecies.damage + " damage.";
					ambushingSpeciesDamageTaken += ambushedCrewMember.getSpecies().damage;
					this.resultText += "\n" + ambushedCrewMember.getName() + " deals " + ambushedCrewMember.getSpecies().damage + " damage.";
				}
				/* After combat see if the crew member is dead. */
				if(ambushedCrewMember.damage(0)){
					this.ship.removeCrewMember(ambushedCrewMember);
					this.resultText += "\n" + ambushedCrewMember.getName() + "' was defeated.";
				}else{
					this.resultText += "\n" + ambushedCrewMember.getName() + "' was victorious and";
					cargoError = this.ship.addCargo(ResourceTypes.FOOD, ambushingSpecies.mass);
					this.resultText += " got " + (ambushingSpecies.mass - cargoError) + " food.";
				}
				break;
		}
	}

	/** Collects a random amount of resources that can be improved by having more scientists.
	 * @param index case 0 collects fuel, case 1 collects metal, case 2 collects water, other cases collect no resources. For backwards compatibility it flows into all of them for case 0.
	 */
	private void executeGatherPlanetResources(int index) {
		int cargoError = 0;
		int potentialResources = 5 + this.ship.getSpecializationQuantity(Specializations.SCIENTIST);
		this.resultText = "Our crew collected: ";
		switch(index){
			case 0:
				/* TODO make a method that can one line this. It depletes checking for underflow, then adds checking for overflow, then replenishes what was overflowed. */
				int fuelHarvestAmount = (int)(Math.random() * potentialResources);
				cargoError = this.planet.depleteResource(ResourceTypes.FUEL, fuelHarvestAmount);
				cargoError = this.ship.addCargo(ResourceTypes.FUEL, fuelHarvestAmount - cargoError);
				this.planet.replenishResource(ResourceTypes.FUEL, cargoError);
				this.resultText += fuelHarvestAmount - cargoError + " fuel ";
				/* TODO break when there are options supported by the UI. */
			case 1:
				int metalHarvestAmount = (int)(Math.random() * potentialResources);
				cargoError = this.planet.depleteResource(ResourceTypes.METAL, metalHarvestAmount);
				cargoError = this.ship.addCargo(ResourceTypes.METAL, metalHarvestAmount - cargoError);
				this.planet.replenishResource(ResourceTypes.METAL, cargoError);
				this.resultText += metalHarvestAmount - cargoError + " metal ";
				/* TODO break when there are options supported by the UI. */
			case 2:
				int waterHarvestAmount = (int)(Math.random() * potentialResources);
				cargoError = this.planet.depleteResource(ResourceTypes.WATER, waterHarvestAmount);
				cargoError = this.ship.addCargo(ResourceTypes.WATER, waterHarvestAmount - cargoError);
				this.planet.replenishResource(ResourceTypes.WATER, cargoError);
				this.resultText += waterHarvestAmount - cargoError + " water ";
				break;
			default:
				/* TODO Maybe hunt for food? */
				this.resultText += "no resources ";
				break;
		}
		this.resultText += "from the planet.";
	}

	/** Cargo is taken if the player loses against the pirates.
	 * @param index case 0 is a ship battle, case 1 pits pirate threat against speed, pilots, and damage where remaining threat is the amount of resources taken. case 2 is crew combat.
	 */
	private void executePirateAttack(int index) {
		StringBuilder sb = new StringBuilder();
		int pirateThreat = this.ship.getPirateThreat() + this.solarSystem.pirateThreat;
		int cargoError = 0;
		switch (index){
			case 0:
				/* TODO Fight them! Similar to the ambush event except combat with the ship. */
			case 1:
				/* Attempt to escape. */
				int maxResourcesTaken = pirateThreat - this.ship.getEngineSpeed() - this.ship.getSpecializationQuantity(Specializations.PILOT) - this.ship.getDamagePerAmmo();
				if(maxResourcesTaken > 0){
					int taken = (int)(Math.random() * maxResourcesTaken);
					sb.append("Pirates overwhelmed our weapons and engines.");
					for(int i = 0; i < taken; i++){
						int resourceType = (int)(Math.random() * 5);
						switch(resourceType){
							case 0:
								cargoError = this.ship.removeCargo(ResourceTypes.AMMO, 1);
								if(cargoError == 0){
									sb.append("\nPirates took 1 Ammo");
								}
								break;
							case 1:
								this.ship.removeCargo(ResourceTypes.FOOD, 1);
								if(cargoError == 0){
									sb.append("\nPirates took 1 Food");
								}
								break;
							case 2:
								this.ship.removeCargo(ResourceTypes.FUEL, 1);
								if(cargoError == 0){
									sb.append("\nPirates took 1 Fuel");
								}
								break;
							case 3:
								this.ship.removeCargo(ResourceTypes.METAL, 1);
								if(cargoError == 0){
									sb.append("\nPirates took 1 Metal");
								}
								break;
							default:
								this.ship.removeCargo(ResourceTypes.WATER, 1);
								if(cargoError == 0){
									sb.append("\nPirates took 1 Water");
								}
								break;
						}
					}
				}else{
					sb.append("Our cargo was protected by our weaons and maneuvers!");
				}
				break;
			case 2:
				/* TODO Ambush them when they board! Similar to the ambush event. */
				break;
			default:
				break;
		}
		this.resultText = sb.toString();
	}

	/** Up to debris rating in damage mitigated by pilots.
	 * @param index is ignored due to there being one outcome.
	 */
	private void executeDebrisStrike(int index) {
		switch(index){
			default:
				int maxPotentialDamage = this.solarSystem.debrisRating - this.ship.getSpecializationQuantity(Specializations.PILOT);
				int damageDealt = (int)(Math.random() * maxPotentialDamage);
				boolean isShipDestroyed = this.ship.damageEngine(damageDealt);
				if (isShipDestroyed) {
					this.resultText = "Our ship took " + damageDealt + " damage from debris and was destroyed!";
					/* TODO Trigger/Signal the end of the game so that the UI displays the lose screen. */
				} else {
					this.resultText = "Our ship took " + damageDealt + " damage from debris!";
				}
				break;
		}

	}

	/** Up to solar radiation in damage mitigated by radiation resistance and engineers.
	 * @param index is ignored due to there being one outcome.
	 */
	private void executeRadiationBurst(int index) {
		switch(index){
			default:
				int maxPotentialDamage = this.solarSystem.solarRadiation - this.ship.getLifeSupportRadiationResistance() - this.ship.getSpecializationQuantity(Specializations.ENGINEER);
				int damageDealt = (int)(Math.random() * maxPotentialDamage);
				boolean isShipDestroyed = this.ship.damageEngine(damageDealt);
				if (isShipDestroyed) {
					this.resultText = "Your ship took " + damageDealt + " damageEngine from a radiation burst and was destroyed!";
					/* TODO Trigger/Signal the end of the game so that the UI displays the lose screen. */
				} else {
					this.resultText = "Your ship took " + damageDealt + " damageEngine from a radiation burst!";
				}
				break;
		}
	}

	/** Nothing game state wise happens but should generate flavor text to keep things interesting.
	 * @param index is ignored due to there being one outcome.
	 */
	private void executePeaceful(int index){
		switch(index){
			case 0:
				resultText = "Nothing happened.";
				break;
			case 1:
				/* TODO Miscellaneous flavor text*/
			default:
				break;
		}
	}
}
