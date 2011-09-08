package de.jbee.panda;

public final class Range {

	private final int min;
	private final int max;

	Range( int min, int max ) {
		super();
		this.min = max > min
			? max
			: min;
		this.max = min > max
			? min
			: max;
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public boolean unboundMin() {
		return min != Integer.MIN_VALUE;
	}

	public boolean unboundMax() {
		return max != Integer.MAX_VALUE;
	}

	public int length() {
		return Math.abs( max - min ) + 1;
	}
}
