package fgm.fgj.gamejamgame;

/** Represents issues related to cargo. */
public class CargoException extends Exception {
	/** The kinds of issues that are represented by CargoExceptions. */
	public enum Problems{
		@SuppressWarnings("javadocs")
		OVERFLOW,
		@SuppressWarnings("javadocs")
		UNDERFLOW,
		@SuppressWarnings("javadocs")
		UNKNOWN_RESOURCE
	}

	/** Cannot be null.
	 * @see Problems
	 */
	private final Problems type;
	/** Represents the quantity associated with the issue, negative values are set to -1 and should be treated as an error value.
	 * For example if an empty cargo with 10 max resource is given 12 resources, an OVERFLOW with quantity 2 is thrown.
	 * An empty cargo has 4 resources taken will result in a UNDERFLOW with quantity 4.
	 */
	private final int quantity;

	/** Initializes a CargoException with the given parameters.
	 * @param type {@link CargoException#type}
	 * @param quantity {@link CargoException#quantity}
	 */
	CargoException(final Problems type, int quantity){
		if(type == null){
			throw new IllegalArgumentException("A cargo exception cannot have a null problem.");
		}
		if(quantity < 0){
			quantity = -1;
		}
		this.type = type;
		this.quantity = quantity;
	}

	/**
	 * @return a Problems that represents the kind of issue encountered.
	 */
	public final Problems getType() {
		return this.type;
	}

	/** @see CargoException#quantity */
	final int getQuantity() {
		return this.quantity;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		switch (this.type){
			case UNKNOWN_RESOURCE:
				sb.append("An unknown resource type was attempted. Perhaps null? Ensure a valid resource type is used before trying again.");
				break;
			case OVERFLOW:
				sb.append("A resource was over stored by ").append(this.quantity);
				break;
			case UNDERFLOW:
				sb.append("A resource was over depleted by ").append(this.quantity);
				break;
			default:
				sb.append("An unknown problem was encountered. Please provide as much detail to a developer at your earliest convenience.");
				break;
		}
		return sb.toString();
	}
}
