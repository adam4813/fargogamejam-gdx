package fgm.fgj.gamejamgame;

public class Engine implements PartModules{
	private int speed;
	private int efficiency;

	/**
	 * @param speed int (0-5) indicating ability to outrun (diminish) threats
	 * @param efficiency int (0-5) indicating fuel efficiency when travelling
	 */
	public Engine(int speed, int efficiency) {
		this.speed = speed;
		this.efficiency = efficiency;
	}
}
