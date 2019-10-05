package fgm.fgj.gamejamgame;

public class Weapon implements PartModules{
	private int damage;
	private WeaponType type;

	public Weapon (int damage, WeaponType type) {
		this.damage = damage;
		this.type = type;
	}
}
