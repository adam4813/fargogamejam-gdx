package fgm.fgj.gamejamgame;


 /**
  * Encapsulates issues related to engines.
  */
public class EngineException extends Exception {
	/**
	 * The kinds of issues that are encapsulated by EngineExceptions.
	 */
	public enum Problems{
		@SuppressWarnings("javadocs")
		DESTROYED
	}
	@SuppressWarnings("javadocs")
	private final Problems type;
	@SuppressWarnings("javadocs")
	EngineException(final Problems type){
		if(type == null){
			throw new IllegalArgumentException("A cargo exception cannot have a null problem.");
		}
		this.type = type;
	}

	/**
	 * @return a Problems that represents the kind of issue encapsulated.
	 */
	public final Problems getType() {
		return this.type;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		switch (this.type){
			case DESTROYED:
				sb.append("The engine has taken too much damageEngine!");
				break;
			default:
				sb.append("An unknown problem was encountered. Please provide as much detail to a developer at your earliest convenience.");
				break;
		}
		return sb.toString();
	}
}
