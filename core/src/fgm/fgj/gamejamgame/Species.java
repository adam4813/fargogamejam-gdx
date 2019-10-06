package fgm.fgj.gamejamgame;

public class Species {
	final int mass;
	final String name;
	final AtmosphericComposition atmosphericCompositionTolerance;
	final int temperatureTolerance;
	final int atmosphericPressureTolerance;
	final int gravityTolerance;
	final int hitPoints;
	final int damage;

	public Species(String name, int gravityTolerance, AtmosphericComposition tolerance, int atmosphericPressureTolerance, int temperatureTolerance, int mass, int hitPoints, int damage) {
		if(name == null){
			throw new IllegalArgumentException("Species cannot be null named.");
		}else if(name == ""){
			throw new IllegalArgumentException("Species cannot be unnamed.");
		}else{
			this.name = name;
		}
		this.gravityTolerance = this.withinRangeOrDefault(gravityTolerance, 0, 5, 2);
		if(tolerance == null){
			throw new IllegalArgumentException("Species must have a preferred atmospheric composition.");
		}else{
			this.atmosphericCompositionTolerance = tolerance;
		}
		this.atmosphericPressureTolerance = this.withinRangeOrDefault(atmosphericPressureTolerance, 0, 5, 2);
		this.temperatureTolerance = this.withinRangeOrDefault(temperatureTolerance, 0, 5, 2);
		this.mass = this.withinRangeOrDefault(mass, 0, 5, 2);
		this.hitPoints = this.withinRangeOrDefault(hitPoints, 2, 10, 5);
		this.damage = this.withinRangeOrDefault(damage, 0, 5, 0);
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
