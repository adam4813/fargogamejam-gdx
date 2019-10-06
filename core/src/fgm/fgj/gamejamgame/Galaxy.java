package fgm.fgj.gamejamgame;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Represents the world map of the game. */
public class Galaxy {
	/** Represents the node that the ship is at, also serving as a root node for the solar systems. */
	private SolarSystem shipLocation;
	private final Ship player;
	public final List<Species> bestiary;
	private static final List<String> nameParts;
	private static final List<String> firstNames;
	private static final List<String> lastNames;
	static{
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
	/** Instantiates the galaxy generating a new map based on the solar system quantity unless root is provided. It will generate the specified number of species if bestiary is empty or null. */
	public Galaxy(int solarSystemQuantity, SolarSystem root, Ship player, int speciesQuantity, List<Species> bestiary){
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

	/** @return the player/ship status. */
	public Ship getShip(){
		return player;
	}

	/** @return the root node of the galaxy */
	public SolarSystem getShipLocation(){
		return shipLocation;
	}

	public void setShipLocation(SolarSystem ss){
		this.shipLocation = ss;
	}

	public Ship generateShip(){
		List<CrewMember> crewMembers = generateCrewMembers();
		Engine engine = new Engine(0, 0, 25, 0);
		Weapon weapon = new Weapon(1, WeaponType.getRandomWeaponType());
		List<AtmosphericComposition> supportedCompositions = new ArrayList<>();
		supportedCompositions.add(crewMembers.get(0).species.atmosphericCompositionTolerance);
		LifeSupportSystem lss = new LifeSupportSystem(supportedCompositions, 1, 2);
		List<PartModules> parts = new ArrayList<>();
		CargoBay cargoBay = new CargoBay(30, 30, 30, 30, 30, 30, 25, 0, 15, 25, 10, 15, parts);

		return new Ship(crewMembers, engine, weapon, lss, cargoBay);
	}

	/**
	 * Builds a rudimentary name for stuff.
	 * @return a name in a format like GEGU-874
	 */
	public static String generateName(){
		StringBuilder solarName = new StringBuilder();
		int nameSize = (int)(Math.random() * 4) + 1;
		for(int i = 0; i < nameSize; i++){
			solarName.append(Galaxy.nameParts.get((int)(Math.random() * nameParts.size())));
		}
		solarName.append('-');
		solarName.append((int)(Math.random() * 1000));
		return solarName.toString();
	}

	public static String generateCrewName(){
		StringBuilder crewName = new StringBuilder();
		crewName.append(Galaxy.firstNames.get((int)(Math.random() * firstNames.size())));
		crewName.append(" ");
		crewName.append(Galaxy.lastNames.get((int)(Math.random() * lastNames.size())));
		return crewName.toString();
	}

	/**
	 *
	 * @return a randomly generated solar system WITHOUT links to other systems.
	 */
	public SolarSystem generateSolarSystem(Boolean hasArtifact){
		String name = Galaxy.generateName();
		StarType starType = StarType.getRandomStarType();
		float starSize = (float)(Math.random() * 3) + .5f;
		List<SolarSystem> links = new ArrayList<>();
		List<Integer> fuelCosts = new ArrayList<>();
		List<Planet> planets = new ArrayList<>();
		int planetQuantity = (int)(Math.random() * 15);
		for(int i = 0; i < planetQuantity; i++){
			planets.add(this.generatePlanet());
		}
		int pirateThreat = (int) (Math.random() * 6);
		int solarRadiation = (int) (Math.random() * 6);
		int debrisRating = (int) (Math.random() * 6);
		// if the solar system has the artifact, put it on a random planet
		if (hasArtifact && planetQuantity > 0) {
			int artifactIndex = (int) (Math.random() * planetQuantity);
			Planet artifactPlanet = planets.get(artifactIndex);
			artifactPlanet.plantArtifact();
		}
		return new SolarSystem(name, starType, starSize, links, fuelCosts, planets, pirateThreat, solarRadiation, debrisRating);
	}

	/**
	 * Generates a galaxy by linking a bunch of solar systems.
	 * @return a root solar system that the galaxy can be traversed from.
	 */
	public SolarSystem generateGalaxyMap(int solarSystemQuantity){
		/* Temporary cache for created solar systems until they are linked and a single root node can be returned. */
		List<SolarSystem> created = new ArrayList<>();
		/* Create the solar systems. */
		Random rand = new Random();
		// One solar system gets an artifact
		int artifactIndex = rand.nextInt(solarSystemQuantity);
		for(int i = 0; i < solarSystemQuantity; i++){
			Boolean addArtifact = (i == artifactIndex);
			SolarSystem ss = this.generateSolarSystem(addArtifact);
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
	 * @return
	 */
	private List<Species> generateBestiary(int speciesQuantity) {
		List<Species> speciesList = new ArrayList();
		for(int i = 0; i <= speciesQuantity; i++){
			speciesList.add(this.generateSpecies());
		}
		return speciesList;
	}

	public Species generateSpecies(){
		String name = generateName();
		int gravityTolerance = (int)(Math.random() * 6);
		AtmosphericComposition atmosphericCompositionTolerance = AtmosphericComposition.getRandomAtmosphere();
		int atmosphericPressureTolerance = (int)(Math.random() * 6);
		int temperatureTolerance = (int)(Math.random() * 6);
		int mass = (int)(Math.random() * 6);
		int hitPoints = (int)(Math.random() * 9) + 2;
		int damage = (int)(Math.random() * 6);
		return new Species(name, IconType.getRandomSpecies(), gravityTolerance, atmosphericCompositionTolerance, atmosphericPressureTolerance, temperatureTolerance, mass, hitPoints, damage);
	}

	/**
	 * Convenience function to get a random species of those available.
	 * @return
	 */
	public Species getRandomSpeciesFromBestiary() {
		Random random = new Random();
		return this.bestiary.get(random.nextInt(this.bestiary.size()));
	}

	private List<CrewMember> generateCrewMembers() {
		List<CrewMember> created = new ArrayList<>();
		Species species = getRandomSpeciesFromBestiary();
		created.add(new CrewMember(this.generateCrewName(), species, Specialization.ENGINEER, 0));
		created.add(new CrewMember(this.generateCrewName(), species, Specialization.PILOT, 0));
		created.add(new CrewMember(this.generateCrewName(), species, Specialization.SCIENTIST, 0));
		created.add(new CrewMember(this.generateCrewName(), species, Specialization.getRandomSpecialization(), 0));
		created.add(new CrewMember(this.generateCrewName(), species, Specialization.getRandomSpecialization(), 0));
		return created;
	}

	/**
	 * Generates a random planet and populates it with species from the bestiary.
	 * @return a planet.
	 */
	public Planet generatePlanet() {
		String name = Galaxy.generateName();
		AtmosphericComposition airType = AtmosphericComposition.getRandomAtmosphere();
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
		return new Planet(name, airType, speciesPresent, gravity, atmospherePressure, temperature, fuel, metals, water);
	}

	public static GameEvent generateGameEvent(SolarSystem solarSystem, int fuelCost, Planet planet, Ship ship) {
		return new GameEvent(solarSystem, fuelCost, planet, ship);
	}
}
