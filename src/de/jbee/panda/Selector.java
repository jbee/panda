package de.jbee.panda;

public class Selector
		implements CharSequence {

	public static final Selector NONE = new Selector( "", 0 );

	private final String value;
	private final int offset;

	private Selector( String value, int offset ) {
		super();
		this.value = value;
		this.offset = Math.min( offset, value.length() );
	}

	public static Selector of( String value ) {
		return new Selector( value, 0 );
	}

	public boolean isNone() {
		return length() == 0;
	}

	public boolean startsWith( String prefix ) {
		return value.startsWith( prefix, offset );
	}

	public Selector skip( int length ) {
		return length < 0 || length() == 0
			? this
			: new Selector( value, offset + length );
	}

	public int parseInt( int def ) {

		return def;
	}

	@Override
	public char charAt( int index ) {
		return value.charAt( offset + index );
	}

	@Override
	public int length() {
		return value.length() - offset;
	}

	@Override
	public CharSequence subSequence( int start, int end ) {
		// TODO Auto-generated method stub
		return null;
	}

}
