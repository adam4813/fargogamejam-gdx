package fgm.fgj.gamejamgame;

public class Weapon implements PartModules{
	final int damage;
	final WeaponType type;

	public Weapon (int damage, WeaponType type) {
		if(type == null){
			throw new IllegalArgumentException("Weapon cannot have a null damage type.");
		}
		this.damage = this.withinRangeOrDefault(damage, 1, 4, 1);
		this.type = type;
	}

	/**
	 * @param value desired value
	 * @param min lowest allowed value
	 * @param max highest allowed value
	 * @param def default value if desired value is out of range
	 * @return value if valid, otherwise default
	 */
	private int withinRangeOrDefault(int value, int min, int max, int def) {
		if (value >= min && value <= max) {
			return value;
		} else {
			return def;
		}
	}

	public int getModuleLevel() {
		int level = 0;
		level += damage - 1;
		if(this.type.equals(WeaponType.PLASMA)){
			level++;
		}
		return Math.min(level, 4);
	}
}
