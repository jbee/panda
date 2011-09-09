package de.jbee.panda;

public class Selector
		implements CharSequence {

	public static final Selector NONE = new Selector( "", 0 );

	private final String value;
	private final int start;

	private Selector( String value, int start ) {
		super();
		this.value = value;
		this.start = Math.min( start, value.length() );
	}

	public static Selector of( String value ) {
		return new Selector( value, 0 );
	}

	public boolean isNone() {
		return length() < 0;
	}

	public boolean startsWith( String prefix ) {
		return value.startsWith( prefix, start );
	}

	public boolean startsWithDigit() {
		return Character.isDigit( value.charAt( start ) );
	}

	public Selector skip( char c ) {
		if ( length() < 0 ) {
			return this;
		}
		final int l = value.length();
		int i = start;
		while ( i < l && value.charAt( i ) == c ) {
			i++;
		}
		if ( i == start ) {
			return this;
		}
		return new Selector( value, i - 1 );
	}

	public Selector skip( int length ) {
		return length < 0 || length() < 0
			? this
			: new Selector( value, start + length );
	}

	public Range parseRange( int minDefault, int maxDefault ) {

		return new Range( minDefault, maxDefault );
	}

	public String readPattern( String regex ) {

		return "";
	}

	public int parseInt( int def ) {
		if ( !startsWithDigit() ) {
			return def;
		}
		return def;
	}

	@Override
	public char charAt( int index ) {
		return value.charAt( start + index );
	}

	@Override
	public int length() {
		return value.length() - start;
	}

	@Override
	public CharSequence subSequence( int start, int end ) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Selector elemAt( int index ) {
		return of( "[" + index + "]" );
	}

}
