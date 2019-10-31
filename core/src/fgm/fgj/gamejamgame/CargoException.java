package fgm.fgj.gamejamgame;

/**
 * Encapsulates issues related to cargo.
 */
public class CargoException extends Exception {
	/**
	 * The kinds of issues that are encapsulated by CargoExceptions.
	 */
	public enum Problems{
		@SuppressWarnings("javadocs")
		OVERFLOW,
		@SuppressWarnings("javadocs")
		UNDERFLOW,
		@SuppressWarnings("javadocs")
		UNKNOWN_RESOURCE
	}
	@SuppressWarnings("javadocs")
	private final Problems type;
	@SuppressWarnings("javadocs")
	private final int quantity;
	@SuppressWarnings("javadocs")
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
	 * @return a Problems that represents the kind of issue encapsulated.
	 */
	public final Problems getType() {
		return this.type;
	}

	/**
	 * @return an int that represents the quantity associated with the issue. For example if an empty cargo with 10 max resource is given 12 resources, an OVERFLOW with quantity 2 is thrown. An empty cargo has 4 resources taken will result in a UNDERFLOW with quantity 4.
	 */
	public final int getQuantity() {
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
