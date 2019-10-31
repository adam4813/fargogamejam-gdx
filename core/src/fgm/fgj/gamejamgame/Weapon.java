package fgm.fgj.gamejamgame;

/** Represents the offensive capabilities of a ship. */
class Weapon implements PartModules{
	/** Represents the amount of damageEngine dealt in combat events, [1..4]. */
	final int damage;
	/** Cannot be null.
	 * @see WeaponType
	 */
	final WeaponType type;

	/** Instantiates a new weapon using the parameters provided unless they aren't in range, then minimal defaults are used.
	 * @param damage {@link Weapon#damage}
	 * @param type {@link Weapon#type}
	 */
	Weapon (int damage, WeaponType type) {
		if(type == null){
			throw new IllegalArgumentException("Weapon cannot have a null damageEngine type.");
		}
		this.damage = Galaxy.initializeWithConstraints(damage, 1, 4, 1);
		this.type = type;
	}

	@Override
	public int getModuleLevel() {
		int level = 0;
		level += damage - 1;
		if(this.type.equals(WeaponType.PLASMA)){
			level++;
		}
		return Math.min(level, 4);
	}
}
