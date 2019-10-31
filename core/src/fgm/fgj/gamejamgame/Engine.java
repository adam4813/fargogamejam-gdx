package fgm.fgj.gamejamgame;

/** Represents the maneuverability and durability of a ship. */
class Engine implements PartModules{
	/** Represents the ship's ability to maneuver mitigating threats, [0..5]. */
	final int speed;
	/** Represents the ship's fuel cost reduction when traveling, [0..5]. */
	final int efficiency;
	/** Represents the ship's resilience to damage, [10..100]. */
	final int hitPoints;
	/** Represents the ship's sustained damage, [0..hitPoints]. */
	private int damageTaken;

	/**
	 * @param speed {@link Engine#speed}
	 * @param efficiency {@link Engine#efficiency}
	 * @param hitPoints {@link Engine#hitPoints}
	 * @param damageTaken {@link Engine#damageTaken}
	 */
	Engine(int speed, int efficiency, int hitPoints, int damageTaken) {
		this.speed = PartModules.initializeWithConstraints(speed, 0, 5, 1);
		this.efficiency = PartModules.initializeWithConstraints(efficiency, 0, 5, 2);
		this.hitPoints = PartModules.initializeWithConstraints(hitPoints, 10, 100, 25);
		this.damageTaken = PartModules.initializeWithConstraints(damageTaken, 0, hitPoints, 0);
	}

	/** The ship is destroyed if damage taken is greater than or equal to hit points.
	 * @param amount represents the damage dealt to the ship. Cannot be negative, will be set to 0.
	 * @throws EngineException if the total damage taken is higher than the engine's hitPoints.
	 */
	void damage(int amount) throws EngineException{
		if(amount < 0){
			amount = 0;
		}
		this.damageTaken = this.damageTaken + amount;
		if (this.damageTaken >= this.hitPoints) {
			throw new EngineException(EngineException.Problems.DESTROYED);
		}
	}

	/** Damage taken cannot be lower than 0 no matter how much is repaired.
	 * @param amount represents the damage removed from the engines. Cannot be negative, will be set to 0.
	 */
	void repair(int amount){
		if(amount < 0){
			amount = 0;
		}
		this.damageTaken = Math.max(this.damageTaken - amount, 0);
	}

	/**
	 * @return an int representing the remaining hit points for the ship.
	 */
	int getCurrentHitPoints(){
		return this.hitPoints - this.damageTaken;
	}

	@Override
	public int getModuleLevel() {
		int level = 0;
		level += efficiency;
		level += speed;
		level += hitPoints;
		return Math.min(level / 3, 4);
	}
}
