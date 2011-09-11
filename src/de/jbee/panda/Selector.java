package de.jbee.panda;

public class Selector
		implements CharSequence {

	public static final Selector NONE = new Selector( "", 0 );

	private final String value;
	private int start;

	private Selector( String value, int start ) {
		super();
		this.value = value;
		this.start = Math.min( start, value.length() );
	}

	public Selector join( Selector sub ) {
		return new Selector( value + sub.toString(), start );
	}

	@Override
	public String toString() {
		return isNone()
			? ""
			: value.substring( start );
	}

	public static Selector of( String value ) {
		return new Selector( value, 0 );
	}

	public Selector fork() {
		return new Selector( value, start );
	}

	public boolean isNone() {
		return length() < 0;
	}

	public boolean startsWith( char prefix ) {
		return charAt( 0 ) == prefix;
	}

	public boolean startsWith( String prefix ) {
		return value.startsWith( prefix, start );
	}

	public Selector skip( char c ) {
		if ( length() < 0 ) {
			return this;
		}
		final int l = value.length();
		while ( start < l && value.charAt( start ) == c ) {
			start++;
		}
		return this;
	}

	public Selector skipNext( int length ) {
		int l = length();
		if ( length > 0 && l > 0 ) {
			start = Math.min( l, start + length );
		}
		return this;
	}

	public String readPattern( String regex ) {

		return "";
	}

	public int integer( int def ) {
		if ( !Character.isDigit( charAt( 0 ) ) ) {
			return def;
		}
		//TODO parse
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
		return new Selector( value.substring( start + start, end ), 0 );
	}

	public static Selector elemAt( int index ) {
		return of( "[" + index + "]" );
	}

	public boolean after( String prefix ) {
		return incIf( prefix.length(), startsWith( prefix ) );
	}

	public boolean after( char c ) {
		return incIf( 1, startsWith( c ) );
	}

	private boolean incIf( int inc, boolean cond ) {
		if ( cond ) {
			start += inc;
		}
		return cond;
	}

	public static Selector range( int start, int end ) {
		return of( "[" + start + ":" + end + "]" );
	}

}
