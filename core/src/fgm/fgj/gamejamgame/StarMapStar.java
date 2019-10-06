package fgm.fgj.gamejamgame;

public class StarMapStar {
	public SolarSystem solarSystem;
	public int fuelCost;

	public StarMapStar(SolarSystem solarSystem, float x, float y, Integer fuelCost) {
		this.solarSystem = solarSystem;
		this.x = (x + 1) / 2;
		this.y = (y + 1) / 2;
		this.fuelCost = fuelCost;
	}

	public float x;
	public float y;
}
