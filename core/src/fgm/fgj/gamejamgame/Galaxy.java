package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

/** Represents the full game state. */
public class Galaxy {
	@SuppressWarnings("javadocs")
	private static final List<String> nameParts;
	@SuppressWarnings("javadocs")
	private static final List<String> firstNames;
	@SuppressWarnings("javadocs")
	private static final List<String> lastNames;

	static{
		/* TODO move names into a file that is loaded in so it isn't cluttering the code.
		*   The file could be loaded on demand or cached into nameParts, firstNames, and lastNames or what have you. */
		nameParts = new ArrayList<>();
		nameParts.add("GA");
		nameParts.add("GE");
		nameParts.add("GI");
		nameParts.add("GO");
		nameParts.add("GU");
		nameParts.add("NA");
		nameParts.add("NE");
		nameParts.add("NI");
		nameParts.add("NO");
		nameParts.add("NU");
		nameParts.add("LA");
		nameParts.add("LE");
		nameParts.add("LI");
		nameParts.add("LO");
		nameParts.add("LU");
		firstNames = new ArrayList<>();
		firstNames.add("Ariosh");
		firstNames.add("Belephor");
		firstNames.add("Caern");
		firstNames.add("Daergo");
		firstNames.add("Eric");
		firstNames.add("Furhman");
		firstNames.add("Galahad");
		firstNames.add("Honorius");
		firstNames.add("Ignus");
		firstNames.add("Jaesrys");
		firstNames.add("Kellon");
		firstNames.add("Leonus");
		firstNames.add("Markos");
		firstNames.add("Nestor");
		firstNames.add("Othar");
		firstNames.add("Persei");
		firstNames.add("Quintus");
		firstNames.add("Rex");
		firstNames.add("Samuel");
		firstNames.add("Torben");
		firstNames.add("Uthgard");
		firstNames.add("Victus");
		firstNames.add("Wendl");
		firstNames.add("Xenapha");
		firstNames.add("Yogur");
		firstNames.add("Zemus");
		lastNames = new ArrayList<>();
		lastNames.add("Aadoms");
		lastNames.add("Bucher");
		lastNames.add("Corinth");
		lastNames.add("Darben");
		lastNames.add("Edrick");
		lastNames.add("Foreman");
		lastNames.add("Gugly");
		lastNames.add("Honebrunk");
		lastNames.add("Iashite");
		lastNames.add("Jakobsson");
		lastNames.add("Kjalberg");
		lastNames.add("Leneer");
		lastNames.add("Mauzer");
		lastNames.add("Nicodemus");
		lastNames.add("Orvarsson");
		lastNames.add("Perguson");
		lastNames.add("Quasimodus");
		lastNames.add("Stephensson");
		lastNames.add("Tuvas");
		lastNames.add("Ursus");
		lastNames.add("Victorius");
		lastNames.add("Worgen");
		lastNames.add("Xenia");
		lastNames.add("Yulian");
		lastNames.add("Zorgenzzon");
	}

	/** The player's avatar. Cannot be null. */
	private final Ship player;
	/** Represents all of the species generated for the galaxy, cannot be null must have at least 1 species. */
	private final List<Species> bestiary;
	/** Represents the node that the ship is at, also serving as a root node for the solar system tree, cannot be null. */
	private SolarSystem shipLocation;

	/** Instantiates the galaxy with the given player. Generating a new map and bestiary if root or bestiary isn't provided respectively.
	 * @param bestiary {@link Galaxy#bestiary}
	 * @param speciesQuantity represents the amount of species to generate for the bestiary. Ignored if bestiary is not null. {@link Galaxy#bestiary}
	 * @param root represents an existing map of solar systems to use in the galaxy. {@link Galaxy#shipLocation}
	 * @param solarSystemQuantity represents the amount of solar systems to generate. Ignored if root is not null. {@link Galaxy#shipLocation}
	 * @param player {@link Galaxy#player} generates one if null.
	 * @see Galaxy#generateGalaxyMap(int)
	 * @see Galaxy#generateBestiary(int)
	 * @see Galaxy#generateShip()
	 */
	Galaxy(List<Species> bestiary, int speciesQuantity, SolarSystem root, int solarSystemQuantity, Ship player){
		if(bestiary == null){
			if(speciesQuantity < 1){
				speciesQuantity = 1;
			}
			bestiary = generateBestiary(speciesQuantity);
		}
		this.bestiary = bestiary;
		if(root == null){
			if(solarSystemQuantity < 1){
				solarSystemQuantity = 1;
			}
			root = generateGalaxyMap(solarSystemQuantity);
		}
		this.shipLocation = root;
		if(player == null){
			player = generateShip();
		}
		this.player = player;
	}

	/** Convenience method to check if the input is within the given constraints.
	 * @param input represents the desired value.
	 * @param min represents the lowest allowed value.
	 * @param max represents the highest allowed value.
	 * @param def represents the value if the desired value is not [min..max].
	 * @return an int equal to input if min <= input <= max, otherwise equal to def.
	 */
	static int initializeWithConstraints(int input, int min, int max, int def) {
		if (input >= min && input <= max) {
			return input;
		}
		return def;
	}

	/** Builds a rudimentary name for stuff.
	 * @return a name in a format like GEGU-874
	 */
	private static String generateCelestialName(){
		StringBuilder solarName = new StringBuilder();
		int nameSize = (int)(Math.random() * 4) + 1;
		for(int i = 0; i < nameSize; i++){
			solarName.append(Galaxy.nameParts.get((int)(Math.random() * nameParts.size())));
		}
		solarName.append('-');
		solarName.append((int)(Math.random() * 1000));
		return solarName.toString();
	}

	/** Builds a name from a list of first names and a list of last names.
	 * @return a name in a format like John Doe
	 */
	static String generateCrewName(){
		StringBuilder crewName = new StringBuilder();
		crewName.append(Galaxy.firstNames.get((int)(Math.random() * firstNames.size())));
		crewName.append(" ");
		crewName.append(Galaxy.lastNames.get((int)(Math.random() * lastNames.size())));
		return crewName.toString();
	}

	/** The game flow. Each call progresses the game by one round, turn, step, etc.
	 * @param solarSystem represents where the game event should be generated.
	 * @param fuelCost represents the fuel cost to get to the provided solar system.
	 * @param planet represents where the game event should be generated.
	 * @param ship represents the player's state.
	 * @return a game event that modifies the game's state.
	 */
	public static GameEvent generateGameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		GameEvent ge = new GameEvent(solarSystem, fuelCost, planet, ship);
		/* TODO temporarily execute on the first option until the UI can handle option selection. */
		ge.execute(0);
		return ge;
	}

	/** @see Galaxy#player */
	public Ship getShip(){
		return player;
	}

	/** @see Galaxy#shipLocation */
	SolarSystem getShipLocation(){
		return shipLocation;
	}

	/**
	 * @param ss represents where the ship should move to, does nothing when null.
	 */
	void setShipLocation(SolarSystem ss){
		if(ss != null){
			this.shipLocation = ss;
		}
	}

	/** Convenience method to instantiate a species with random attributes.
	 * @return a species for the bestiary.
	 * @see AtmosphericCompositions
	 * @see Species#Species(String, IconType, AtmosphericCompositions, int, int, int, int, int, int)
	 */
	private Species generateSpecies(){
		String name = generateCelestialName();
		AtmosphericCompositions atmosphericCompositionTolerance = AtmosphericCompositions.getRandomAtmosphere();
		int atmosphericPressureTolerance = (int)(Math.random() * 6);
		int temperatureTolerance = (int)(Math.random() * 6);
		int gravityTolerance = (int)(Math.random() * 6);
		int mass = (int)(Math.random() * 6);
		int hitPoints = (int)(Math.random() * 9) + 2;
		int damage = (int)(Math.random() * 6);
		return new Species(name, IconType.getRandomSpecies(), atmosphericCompositionTolerance, atmosphericPressureTolerance, temperatureTolerance, gravityTolerance, mass, hitPoints, damage);
	}

	/** Convenience method that generates the given number of species.
	 * @return a list of species that represent at least 1 species generated for the bestiary.
	 * @see Galaxy#generateSpecies()
	 */
	private List<Species> generateBestiary(int speciesQuantity) {
		if(speciesQuantity < 1){
			speciesQuantity = 1;
		}
		List<Species> speciesList = new ArrayList<>();
		for(int i = 0; i < speciesQuantity; i++){
			speciesList.add(this.generateSpecies());
		}
		return speciesList;
	}

	/** Convenience function to get a random species of those available.
	 * @return a species that represents a randomly selected species from the bestiary.
	 * @see Galaxy#bestiary
	 */
	private Species getRandomSpeciesFromBestiary() {
		return this.bestiary.get((int)(Math.random() * this.bestiary.size()));
	}

	/** Convenience method to instantiate 3 crew members of a single species. At least one of each specialization will be among them.
	 * @return a list of CrewMember that represents a ship crew.
	 * @see Specializations
	 * @see CrewMember#CrewMember(String, Species, Specializations, int)
	 * @see Galaxy#generateCrewName()
	 */
	private List<CrewMember> generateCrewMembers() {
		List<CrewMember> created = new ArrayList<>();
		Species species = getRandomSpeciesFromBestiary();
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.ENGINEER, 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.PILOT, 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.SCIENTIST, 0));
		return created;
	}

	/** Instantiates a minimal default ship and generates a new crew for the ship. It is stocked with essential resources to start the player's journey.
	 * @return a Ship that represents the player's avatar.
	 * @see Galaxy#generateCrewMembers()
	 * @see Ship#Ship(List, Engine, Weapon, LifeSupportSystem, CargoBay, int)
	 */
	private Ship generateShip(){
		List<CrewMember> crewMembers = generateCrewMembers();
		Engine engine = new Engine(0, 0, 10, 0);
		Weapon weapon = new Weapon(1, WeaponType.BALLISTIC);
		List<AtmosphericCompositions> supportedCompositions = new ArrayList<>();
		supportedCompositions.add(crewMembers.get(0).species.atmosphericCompositionTolerance);
		LifeSupportSystem lss = new LifeSupportSystem(supportedCompositions, 0, 0);
		List<PartModules> parts = new ArrayList<>();
		CargoBay cargoBay = new CargoBay(false,10, 10, 10, 10, 10, 10, 6, 0, 10, 10, 4, 0, parts);
		return new Ship(crewMembers, engine, weapon, lss, cargoBay, 0);
	}

	/** Convenience method to generate a planet with random attributes.
	 * @return a planet to be added to a solar system.
	 * @see Galaxy#generateCelestialName()
	 * @see AtmosphericCompositions
	 * @see Galaxy#getRandomSpeciesFromBestiary()
	 * @see Planet#Planet(String, AtmosphericCompositions, List, int, int, int, int, int, int, boolean)
	 */
	private Planet generatePlanet(boolean hasArtifact) {
		String name = Galaxy.generateCelestialName();
		AtmosphericCompositions airType = AtmosphericCompositions.getRandomAtmosphere();
		List<Species> speciesPresent = new ArrayList<>();
		int speciesPresentQuantity = (int)(Math.random() * 3);
		for(int i = 0; i < speciesPresentQuantity; i++){
			speciesPresent.add(this.getRandomSpeciesFromBestiary());
		}
		int gravity = (int)(Math.random() * 6);
		int atmospherePressure = (int)(Math.random() * 6);
		int temperature = (int)(Math.random() * 6);
		int fuel = (int)(Math.random() * 101);
		int metals = ((int)(Math.random() * 151)) + 100;
		int water = (int)(Math.random() * 251);
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure, temperature, fuel, metals, water, hasArtifact);
	}

	/** Convenience method that generates a solar system with random attributes.
	 * If it has the artifact it will plant the artifact on one of its planets.
	 * It will NOT link it with other solar systems and will need to be linked somewhere else.
	 * @param hasArtifact represents, when true, that the artifact should be on a planet in the system.
	 * @return a randomly generated solar system WITHOUT links to other systems.
	 * @see SolarSystem#SolarSystem(String, StarType, float, List, List, List, int, int, int)
	 * @see SolarSystem#linkSolarSystem(SolarSystem, int)
	 */
	private SolarSystem generateSolarSystem(boolean hasArtifact){
		String name = Galaxy.generateCelestialName();
		StarType starType = StarType.getRandomStarType();
		float starSize = (float)(Math.random() * 3) + .5f;
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int planetQuantity = (int)(Math.random() * 12);
		if(hasArtifact){
			/* Ensure there is at least one planet for the artifact to be on. */
			planetQuantity++;
		}
		int artifactIndex = (int) (Math.random() * planetQuantity);
		for(int i = 0; i < planetQuantity; i++){
			planets.add(this.generatePlanet(hasArtifact && artifactIndex == i));
		}
		int pirateThreat = (int) (Math.random() * 6);
		int solarRadiation = (int) (Math.random() * 6);
		int debrisRating = (int) (Math.random() * 6);
		return new SolarSystem(name, starType, starSize, links, fuelCosts, planets, pirateThreat, solarRadiation, debrisRating);
	}

	/** Convenience method that generates a graph of solar systems with random attributes.
	 * @return a root solar system that the galaxy can be traversed from.
	 * @see Galaxy#generateSolarSystem(boolean)
	 * @see SolarSystem#linkSolarSystem(SolarSystem, int)
	 */
	private SolarSystem generateGalaxyMap(int solarSystemQuantity){
		/* Temporary cache for created solar systems until they are linked and a single root node can be returned. */
		List<SolarSystem> created = new ArrayList<>();
		/* Create the solar systems one will have the artifact. */
		int artifactIndex = (int)(Math.random() * solarSystemQuantity);
		for(int i = 0; i < solarSystemQuantity; i++){
			SolarSystem ss = this.generateSolarSystem(i == artifactIndex);
			created.add(ss);
		}
		/* Link the solar systems. */
		for(SolarSystem ss : created){
			int links = (int)(Math.random() * 5) + 1;
			for(int i = 0; i < links; i++){
				/* Each link picks a random index to connect to.
				 * Generates a fuel cost.
				 * Makes the link bidirectional.
				 * This also ensures that both ways have the same fuelCosts. */
				int linkIndex = (int)(Math.random() * created.size());
				int fuelCost = (int)(Math.random() * 4) + 2;
				ss.linkSolarSystem(created.get(linkIndex), fuelCost);
				created.get(linkIndex).linkSolarSystem(ss, fuelCost);
			}
		}
		/* Return one as a starting location. */
		return created.get(0);
	}
}
