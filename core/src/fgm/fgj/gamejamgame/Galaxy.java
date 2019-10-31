package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Represents the full game state. */
public class Galaxy {
	@SuppressWarnings("javadocs")
	private static final List<String> nameParts;
	@SuppressWarnings("javadocs")
	private static final List<String> firstNames;
	@SuppressWarnings("javadocs")
	private static final List<String> lastNames;

	static{
		/* TODO move names into a file that is loaded in so it isn't cluttering the code. */
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

	@SuppressWarnings("javadocs")
	private final Ship player;
	@SuppressWarnings("javadocs")
	private final List<Species> bestiary;
	/** Represents the node that the ship is at, also serving as a root node for the solar systems. */
	private SolarSystem shipLocation;
	/** Instantiates the galaxy generating a new map based on the solar system quantity unless root is provided. It will generate the specified number of species if bestiary is empty or null. */
	Galaxy(int solarSystemQuantity, SolarSystem root, Ship player, int speciesQuantity, List<Species> bestiary){
		if(solarSystemQuantity < 1){
			throw new IllegalArgumentException("A galaxy must consist of at least 1 solar system.");
		}
		if(bestiary == null){
			this.bestiary = generateBestiary(speciesQuantity);
		}else if(bestiary.size() < 1){
			this.bestiary = generateBestiary(speciesQuantity);
		}else{
			this.bestiary = bestiary;
		}
		if(root == null){
			this.shipLocation = generateGalaxyMap(solarSystemQuantity);
		}else{
			this.shipLocation = root;
		}
		if(player == null){
			this.player = generateShip();
		}else{
			this.player = player;
		}
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

	/**
	 * Builds a rudimentary name for stuff.
	 * @return a name in a format like GEGU-874
	 */
	private static String generateName(){
		StringBuilder solarName = new StringBuilder();
		int nameSize = (int)(Math.random() * 4) + 1;
		for(int i = 0; i < nameSize; i++){
			solarName.append(Galaxy.nameParts.get((int)(Math.random() * nameParts.size())));
		}
		solarName.append('-');
		solarName.append((int)(Math.random() * 1000));
		return solarName.toString();
	}

	static String generateCrewName(){
		StringBuilder crewName = new StringBuilder();
		crewName.append(Galaxy.firstNames.get((int)(Math.random() * firstNames.size())));
		crewName.append(" ");
		crewName.append(Galaxy.lastNames.get((int)(Math.random() * lastNames.size())));
		return crewName.toString();
	}

	public static GameEvent generateGameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		return new GameEvent(solarSystem, fuelCost, planet, ship);
	}

	/** @return the player/ship status. */
	public Ship getShip(){
		return player;
	}

	/** @return the root node of the galaxy */
	SolarSystem getShipLocation(){
		return shipLocation;
	}

	void setShipLocation(SolarSystem ss){
		this.shipLocation = ss;
	}

	private Ship generateShip(){
		List<CrewMember> crewMembers = generateCrewMembers();
		Engine engine = new Engine(0, 0, 25, 0);
		Weapon weapon = new Weapon(1, WeaponType.getRandomWeaponType());
		List<AtmosphericCompositions> supportedCompositions = new ArrayList<>();
		supportedCompositions.add(crewMembers.get(0).species.atmosphericCompositionTolerance);
		LifeSupportSystem lss = new LifeSupportSystem(supportedCompositions, 1, 2);
		List<PartModules> parts = new ArrayList<>();
		CargoBay cargoBay = new CargoBay(true,25, 25, 25, 25, 25, 25, 10, 10, 10, 20, 5, 5, parts);

		return new Ship(crewMembers, engine, weapon, lss, cargoBay);
	}

	/**
	 *
	 * @return a randomly generated solar system WITHOUT links to other systems.
	 */
	private SolarSystem generateSolarSystem(boolean hasArtifact){
		String name = Galaxy.generateName();
		StarType starType = StarType.getRandomStarType();
		float starSize = (float)(Math.random() * 3) + .5f;
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int planetQuantity = (int)(Math.random() * 15);
		int artifactIndex = (int) (Math.random() * planetQuantity);
		for(int i = 0; i < planetQuantity; i++){
			planets.add(this.generatePlanet(hasArtifact && artifactIndex == i));
		}
		int pirateThreat = (int) (Math.random() * 6);
		int solarRadiation = (int) (Math.random() * 6);
		int debrisRating = (int) (Math.random() * 6);
		return new SolarSystem(name, starType, starSize, links, fuelCosts, planets, pirateThreat, solarRadiation, debrisRating);
	}

	/**
	 * Generates a galaxy by linking a bunch of solar systems.
	 * @return a root solar system that the galaxy can be traversed from.
	 */
	private SolarSystem generateGalaxyMap(int solarSystemQuantity){
		/* Temporary cache for created solar systems until they are linked and a single root node can be returned. */
		List<SolarSystem> created = new ArrayList<>();
		/* Create the solar systems. */
		Random rand = new Random();
		// One solar system gets an artifact
		int artifactIndex = rand.nextInt(solarSystemQuantity);
		for(int i = 0; i < solarSystemQuantity; i++){
			SolarSystem ss = this.generateSolarSystem(i == artifactIndex);
			created.add(ss);
		}
		/* Link the solar systems. */
		for(SolarSystem ss : created){
			int links = (int)(Math.random() * 6) + 1;
			for(int i = 0; i < links; i++){
				int linkIndex = (int)(Math.random() * created.size());
				int fuelCost = (int)(Math.random() * 4) + 1;
				if(!ss.linkedSolarSystems.contains(created.get(linkIndex))){
					/* If the link doesn't already exist make it bidirectional to avoid unreachable solar systems.
					* This also ensures that both ways have the same fuelCosts. */
					ss.linkSolarSystem(created.get(linkIndex), fuelCost);
					created.get(linkIndex).linkSolarSystem(ss, fuelCost);
				}
			}
		}
		/* Return one as a starting location. */
		return created.get(0);
	}

	/**
	 * Builds a bestiary of species available in the galaxy.
	 * @return a list of species that represent all species in the galaxy.
	 */
	private List<Species> generateBestiary(int speciesQuantity) {
		List<Species> speciesList = new ArrayList<>();
		for(int i = 0; i <= speciesQuantity; i++){
			speciesList.add(this.generateSpecies());
		}
		return speciesList;
	}

	private Species generateSpecies(){
		String name = generateName();
		AtmosphericCompositions atmosphericCompositionTolerance = AtmosphericCompositions.getRandomAtmosphere();
		int atmosphericPressureTolerance = (int)(Math.random() * 6);
		int temperatureTolerance = (int)(Math.random() * 6);
		int gravityTolerance = (int)(Math.random() * 6);
		int mass = (int)(Math.random() * 6);
		int hitPoints = (int)(Math.random() * 9) + 2;
		int damage = (int)(Math.random() * 6);
		return new Species(name, IconType.getRandomSpecies(), atmosphericCompositionTolerance, atmosphericPressureTolerance, temperatureTolerance, gravityTolerance, mass, hitPoints, damage);
	}

	/**
	 * Convenience function to get a random species of those available.
	 * @return a species that represents a species from the bestiary.
	 */
	private Species getRandomSpeciesFromBestiary() {
		Random random = new Random();
		return this.bestiary.get(random.nextInt(this.bestiary.size()));
	}

	private List<CrewMember> generateCrewMembers() {
		List<CrewMember> created = new ArrayList<>();
		Species species = getRandomSpeciesFromBestiary();
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.ENGINEER, 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.PILOT, 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.SCIENTIST, 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.getRandomSpecialization(), 0));
		created.add(new CrewMember(Galaxy.generateCrewName(), species, Specializations.getRandomSpecialization(), 0));
		return created;
	}

	/**
	 * Generates a random planet and populates it with species from the bestiary.
	 * @return a planet.
	 */
	private Planet generatePlanet(boolean hasArtifact) {
		String name = Galaxy.generateName();
		AtmosphericCompositions airType = AtmosphericCompositions.getRandomAtmosphere();
		List<Species> speciesPresent = new ArrayList<>();
		int speciesPresentQuantity = (int)(Math.random() * 3);
		for(int i = 0; i < speciesPresentQuantity; i++){
			speciesPresent.add(this.getRandomSpeciesFromBestiary());
		}
		int gravity = (int)(Math.random() * 6);
		int atmospherePressure = (int)(Math.random() * 6);
		int temperature = (int)(Math.random() * 6);
		int fuel = (int)(Math.random() * 126);
		int metals = (int)(Math.random() * 126);
		int water = (int)(Math.random() * 126);
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure, temperature, fuel, metals, water, hasArtifact);
	}
}
