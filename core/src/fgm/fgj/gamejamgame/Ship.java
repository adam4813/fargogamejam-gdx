package fgm.fgj.gamejamgame;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Represents the player. */
public class Ship {
	/** Represents passengers and crew on the ship. Cannot be null or empty.
	 * @see CrewMember
	 */
	private List<CrewMember> crewMembers;
	/** Represents everything stored in the ship. Cannot be null.
	 * @see CargoBay
	 */
	private CargoBay cargoBay;
	/** Represents the ship's installed engine. Cannot be null.
	 * @see Engine
	 */
	private Engine engine;
	/** Represents the ship's installed life support system. Cannot be null.
	 * @see LifeSupportSystem
	 */
	private LifeSupportSystem lifeSupport;
	/** Represents the ship's installed weapon systems. Cannot be null
	 * @see Weapon
	 */
	private Weapon weapon;

	/** Instantiates (builds) a new ship with the given modules and crew.
	 * @param crewMembers {@link Ship#crewMembers}
	 * @param engine {@link Ship#engine}
	 * @param weapon {@link Ship#weapon}
	 * @param lss {@link Ship#lifeSupport}
	 * @param cargoBay {@link Ship#cargoBay}
	 */
	public Ship(List<CrewMember> crewMembers, Engine engine, Weapon weapon, LifeSupportSystem lss, CargoBay cargoBay) {
		if (crewMembers == null) {
			throw new IllegalArgumentException("A ship cannot be operated with a null crew.");
		} else if (crewMembers.isEmpty()) {
			throw new IllegalArgumentException("A ship cannot be operated with an empty crew.");
		}
		if (engine == null) {
			throw new IllegalArgumentException("A ship cannot be fly with a null engine.");
		}
		if (weapon == null) {
			throw new IllegalArgumentException("A ship cannot be complete with a null weapon.");
		}
		if (lss == null) {
			throw new IllegalArgumentException("A ship cannot host a crew with a null life support system.");
		}
		if (cargoBay == null) {
			throw new IllegalArgumentException("A ship cannot be complete with a null cargo bay.");
		}
		this.crewMembers = crewMembers;
		this.engine = engine;
		this.weapon = weapon;
		this.lifeSupport = lss;
		this.cargoBay = cargoBay;
	}

	/** @see CargoBay#storeResource(ResourceTypes, int) */
	public void addCargo(ResourceTypes type, int quantity) throws CargoException{
		this.cargoBay.storeResource(type, quantity);
	}

	/** @see CargoBay#depleteResource(ResourceTypes, int) */
	public void removeCargo(ResourceTypes type, int quantity) throws CargoException{
		this.cargoBay.depleteResource(type, quantity);
	}

	/** @see CargoBay#checkResource(ResourceTypes) */
	public int checkCargo(ResourceTypes type){
		return this.cargoBay.checkResource(type);
	}

	/** @see CargoBay#storePart(PartModules) */
	public void addCargo(PartModules module){
		this.cargoBay.storePart(module);
	}

	/** @see CargoBay#removepart(PartModules) */
	public void removeCargo(PartModules module){
		this.cargoBay.removepart(module);
	}

	/** @see CargoBay#getStoredParts() */
	public List<PartModules> getPartsInCargo(){
		return this.cargoBay.getStoredParts();
	}

	/** Switches the current module with the provided one and stores the old one in cargo.
	 * @param module represents the module to be used from now on. Won't have any effect if null.
	 * @return a boolean that, when true, represents the formerly used module was added to the cargo and the provided one is equipped for use from now on.
	 * @see CargoBay#transferCargoTo(CargoBay) is called in the case of swapping out a CargoBay. This may result in lost resources if the new cargo bay is smaller in that capacity.
	 */
	public boolean swapPartModule(PartModules module){
		PartModules oldModule = null;
		if(module instanceof CargoBay && !this.cargoBay.equals(module)){
			oldModule = this.cargoBay;
			this.cargoBay = (CargoBay)module;
			((CargoBay)oldModule).transferCargoTo(this.cargoBay);
		}else if(module instanceof Engine && !this.engine.equals(module)){
			oldModule = this.engine;
			this.engine = (Engine)module;
		}else if(module instanceof LifeSupportSystem && !this.lifeSupport.equals(module)){
			oldModule = this.lifeSupport;
			this.lifeSupport = (LifeSupportSystem) module;
		}else if(module instanceof Weapon && !this.weapon.equals(module)){
			oldModule = this.weapon;
			this.weapon = (Weapon)module;
		}
		try{
			/* Attempt to add the old module to cargo. */
			this.addCargo(oldModule);
			return true;
		}catch(IllegalArgumentException caught){
			/* Null was attempted and therefore the swap didn't work. */
			return false;
		}
	}

	/** @see Engine#damage(int) */
	public void damageEngine(int damageAmount) throws EngineException{
		this.engine.damage(damageAmount);
	}

	/** @see Engine#repair(int) */
	public void repairEngine(int repairAmount) {
		this.engine.repair(repairAmount);
	}

	/** @see Engine#speed */
	public int getEngineSpeed() {
		return this.engine.speed;
	}

	/** @see Engine#efficiency */
	public int getEngineEfficiency() {
		return this.engine.efficiency;
	}

	/** @see Engine#hitPoints */
	public int getEngineHitPoints(){
		return this.engine.hitPoints;
	}

	/** @see Engine#getCurrentHitPoints() */
	public int getEngineCurrentHitPoints() throws EngineException{
		return this.engine.getCurrentHitPoints();
	}

	/** @see LifeSupportSystem#solarRadiationTolerance */
	public int getLifeSupportRadiationResistance() {
		return this.lifeSupport.solarRadiationTolerance;
	}

	/** @see LifeSupportSystem#gasEfficiency */
	public int getLifeSupportGasEfficiency(){
		return this.lifeSupport.gasEfficiency;
	}

	/** @see LifeSupportSystem#getSupportedAtmosphereCompositions() */
	public List<AtmosphericCompositions> getSupportedAtmosphericCompositions(){
		return this.lifeSupport.getSupportedAtmosphereCompositions();
	}

	/** @see Weapon#damage */
	public int getDamagePerAmmo() {
		return this.weapon.damage;
	}

	/** @see Weapon#type */
	public WeaponType getWeaponType(){
		return this.weapon.type;
	}

	/**
	 * @return an unmodifiable list of the crew members. Intended for display purposes.
	 * @see Ship#removeCrewMember(CrewMember)
	 * @see Ship#addCrewMember(CrewMember)
	 */
	public List<CrewMember> getCrewMembers() {
		return Collections.unmodifiableList(this.crewMembers);
	}

	/** @param crewMember represents the crew member to remove, does nothing when null. */
	void removeCrewMember(CrewMember crewMember) {
		if (crewMember != null) {
			this.crewMembers.remove(crewMember);
		}
	}

	/** @return a set of the species representing those among the crew members. */
	Set<Species> getCrewSpecies() {
		Set<Species> crewSpecies = new HashSet();
		for(CrewMember cm : this.getCrewMembers()){
			crewSpecies.add(cm.species);
		}
		return crewSpecies;
	}

	/** @param crewMember represents the new recruit to add to the crew, does nothing when null. */
	void addCrewMember(CrewMember crewMember) {
		if (crewMember != null) {
			this.crewMembers.add(crewMember);
		}
	}

	/** @see PartModules#getModuleLevel() */
	public int getCargoBayModuleLevel(){
		return this.cargoBay.getModuleLevel();
	}

	/** @see PartModules#getModuleLevel() */
	public int getEngineModuleLevel(){
		return this.engine.getModuleLevel();
	}

	/** @see PartModules#getModuleLevel() */
	public int getLifeSupportSystemsModuleLevel(){
		return this.lifeSupport.getModuleLevel();
	}

	/** @see PartModules#getModuleLevel() */
	public int getWeaponModuleLevel(){
		return this.weapon.getModuleLevel();
	}
}
