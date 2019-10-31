package fgm.fgj.gamejamgame;

import java.util.Collections;
import java.util.List;

/** Represents the crew survival capabilities of a ship. */
class LifeSupportSystem implements PartModules {
	/** Represents the reduction of gas consumed by the crew members, [0..5]. */
	final int gasEfficiency;
	/** Represents the reduction of damage from radiation to crew members, [0..5]. */
	final int solarRadiationTolerance;
	/** Represents the AtmosphericCompositions that the crew may breathe and be compatible with the ship. Cannot be null or empty. */
	private final List<AtmosphericCompositions> supportedAtmosphereCompositions;

	/** Instantiates a new life support system with the given parameters, unless they are out of range. Then minimal defaults are used.
	 * @param supportedAtmosphereCompositions {@link LifeSupportSystem#supportedAtmosphereCompositions}
	 * @param solarRadiationTolerance {@link LifeSupportSystem#solarRadiationTolerance}
	 * @param gasEfficiency {@link LifeSupportSystem#gasEfficiency}
	 */
	LifeSupportSystem (List<AtmosphericCompositions> supportedAtmosphereCompositions, int solarRadiationTolerance, int gasEfficiency ) {
		if(supportedAtmosphereCompositions == null){
			throw new IllegalArgumentException("A life support system cannot have a null supported compositions of atmosphere.");
		}else if (supportedAtmosphereCompositions.isEmpty()){
			throw new IllegalArgumentException("A life support system is worthless without supported atmosphere composition.");
		}
		this.supportedAtmosphereCompositions = supportedAtmosphereCompositions;
		this.solarRadiationTolerance = Galaxy.initializeWithConstraints(solarRadiationTolerance, 0, 5, 0);
		this.gasEfficiency = Galaxy.initializeWithConstraints(gasEfficiency, 0, 5, 0);
	}

	/** @return an unmodifiable List of AtmosphericCompositions that represent the kinds of atmosphere crew may breath and be compatible with the ship. */
	List<AtmosphericCompositions> getSupportedAtmosphereCompositions(){
		return Collections.unmodifiableList(this.supportedAtmosphereCompositions);
	}

	@Override
	public int getModuleLevel() {
		int level = 0;
		level += solarRadiationTolerance;
		level += gasEfficiency;
		return Math.min(level / 2, 4);
	}
}
