package fgm.fgj.gamejamgame;

public class Engine implements PartModules{
	final int speed;
	final int efficiency;
	final int hitPoints;
	int damageTaken;

	/**
	 * @param speed int (0-5) indicating ability to outrun (diminish) threats
	 * @param efficiency int (0-5) indicating fuel efficiency when travelling
	 */
	public Engine(int speed, int efficiency, int hitPoints, int damageTaken) {
		this.speed = this.withinRangeOrDefault(speed, 0, 5, 1);
		this.efficiency = this.withinRangeOrDefault(efficiency, 0, 5, 2);
		this.hitPoints = this.withinRangeOrDefault(hitPoints, 10, 100, 25);
		this.damageTaken = this.withinRangeOrDefault(damageTaken, 0, hitPoints, 0);
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
}
