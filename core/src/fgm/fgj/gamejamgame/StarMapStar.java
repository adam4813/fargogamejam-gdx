package fgm.fgj.gamejamgame;

public class StarMapStar {
	public SolarSystem solarSystem;

	public StarMapStar(SolarSystem solarSystem, float x, float y) {
		this.solarSystem = solarSystem;
		this.x = (x + 1) / 2;
		this.y = (y + 1) / 2;
	}

	public float x;
	public float y;
}
