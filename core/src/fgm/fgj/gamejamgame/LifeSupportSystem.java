package fgm.fgj.gamejamgame;

import java.util.ArrayList;
import java.util.List;

public class LifeSupportSystem implements PartModules {
	private int solarRadiationTolerance;
	private List<AtmosphericComposition> supportedAtmosphereCompositions;
	private int atmosphericEfficency;

	public LifeSupportSystem (int solarRadiationTolerance, List<AtmosphericComposition> supportedAtmosphereCompositions, int atmosphericEfficency ) {
		this.solarRadiationTolerance = solarRadiationTolerance;
		this.supportedAtmosphereCompositions = supportedAtmosphereCompositions;
		this.atmosphericEfficency = atmosphericEfficency;
	}

	public int getSolarRadiationTolerance() {
		return this.solarRadiationTolerance;
	}
}
