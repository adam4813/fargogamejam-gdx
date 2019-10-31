package fgm.fgj.gamejamgame;

 /** Represents issues related to engines. */
public class EngineException extends Exception {
	/** The kinds of issues that are represented by EngineExceptions. */
	public enum Problems{
		@SuppressWarnings("javadocs")
		DESTROYED
	}

	/** Cannot be null.
	 * @see Problems
	 */
	private final Problems type;

	 /**
	  * @param type {@link EngineException#type}
	  */
	EngineException(final Problems type){
		if(type == null){
			throw new IllegalArgumentException("A cargo exception cannot have a null problem.");
		}
		this.type = type;
	}

	/**
	 * @see EngineException.Problems
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
