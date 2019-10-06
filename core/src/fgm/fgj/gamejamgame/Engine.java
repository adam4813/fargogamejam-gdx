package fgm.fgj.gamejamgame;

public class Engine implements PartModules{
	final int speed;
	final int efficiency;

	/**
	 * @param speed int (0-5) indicating ability to outrun (diminish) threats
	 * @param efficiency int (0-5) indicating fuel efficiency when travelling
	 */
	public Engine(int speed, int efficiency) {
		this.speed = this.withinRangeOrDefault(speed, 0, 5, 1);
		this.efficiency = this.withinRangeOrDefault(efficiency, 0, 5, 2);
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
