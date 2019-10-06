package fgm.fgj.gamejamgame;

import java.util.List;

public class LifeSupportSystem implements PartModules {
	final int solarRadiationTolerance;
	final List<AtmosphericComposition> supportedAtmosphereCompositions;
	/** Amount of gas consumed per crew member reduction. */
	final int gasEfficiency;

	public LifeSupportSystem (List<AtmosphericComposition> supportedAtmosphereCompositions, int solarRadiationTolerance, int gasConsumption ) {
		if(supportedAtmosphereCompositions == null){
			throw new IllegalArgumentException("A life support system cannot have a null supported compositions of atmosphere.");
		}else if (supportedAtmosphereCompositions.isEmpty()){
			throw new IllegalArgumentException("A life support system is worthless without supported atmosphere composition.");
		}
		this.supportedAtmosphereCompositions = supportedAtmosphereCompositions;
		this.solarRadiationTolerance = this.withinRangeOrDefault(solarRadiationTolerance, 0, 5, 2);
		this.gasEfficiency = this.withinRangeOrDefault(gasConsumption, 0, 5, 0);
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
