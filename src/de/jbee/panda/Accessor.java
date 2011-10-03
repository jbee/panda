package de.jbee.panda;

public class Accessor
		implements CharSequence {

	public static final Accessor EMPTY = new Accessor( "", 0 );

	private final String value;
	private int start;

	private Accessor( String value, int start ) {
		super();
		this.value = value;
		this.start = Math.min( start, value.length() );
	}

	public Accessor join( Accessor sub ) {
		return new Accessor( value + sub.toString(), start );
	}

	@Override
	public String toString() {
		return isEmpty()
			? ""
			: value.substring( start );
	}

	public static Accessor of( String value ) {
		return new Accessor( value, 0 );
	}

	public Accessor fork() {
		return new Accessor( value, start );
	}

	public boolean isEmpty() {
		return length() < 0;
	}

	public boolean startsWith( char prefix ) {
		return charAt( 0 ) == prefix;
	}

	public boolean startsWith( String prefix ) {
		return value.startsWith( prefix, start );
	}

	public Accessor gobble( String prefix ) {
		if ( startsWith( prefix ) ) {
			start += prefix.length();
		}
		return this;
	}

	public Accessor gobble( char prefix ) {
		if ( startsWith( prefix ) ) {
			start++;
		}
		return this;
	}

	public Accessor gobbleAll( char c ) {
		if ( length() < 0 ) {
			return this;
		}
		final int l = value.length();
		while ( start < l && value.charAt( start ) == c ) {
			start++;
		}
		return this;
	}

	public Accessor gobbleN( int length ) {
		int l = length();
		if ( length > 0 && l > 0 ) {
			start = Math.min( l, start + length );
		}
		return this;
	}

	public int index( int defaultIndex ) {
		if ( !Character.isDigit( charAt( 0 ) ) ) {
			return defaultIndex;
		}
		StringBuilder b = new StringBuilder();
		int i = 0;
		while ( i < length() && Character.isDigit( charAt( i ) ) ) {
			b.append( charAt( i ) );
			i++;
		}
		final int l = b.length();
		start += l;
		return Integer.parseInt( value.substring( start - l, start ) );
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
		return new Accessor( value.substring( start + start, end ), 0 );
	}

	public static Accessor elemAt( int index ) {
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

	public static Accessor range( int start, int end ) {
		return of( "[" + start + ":" + end + "]" );
	}

	public String property( String defaultProperty ) {
		if ( !Character.isLetter( charAt( 0 ) ) ) {
			return defaultProperty;
		}
		StringBuilder b = new StringBuilder();
		int i = 0;
		while ( i < length() && Character.isLetter( charAt( i ) ) ) {
			b.append( charAt( i ) );
			i++;
		}
		final int l = b.length();
		start += l;
		return value.substring( start - l, start );
	}

	public String until( char c ) {
		int i = 0;
		while ( i < length() && charAt( i ) != c ) {
			i++;
		}
		if ( i >= length() ) {
			String res = value.substring( start );
			start = value.length();
			return res;
		}
		int l = i - 1;
		start += l;
		return value.substring( start - l, start );
	}

	public String untilWhitespace() {
		int i = 0;
		while ( i < length() && !Character.isWhitespace( charAt( i ) ) ) {
			i++;
		}
		if ( i >= length() ) {
			String res = value.substring( start );
			start = value.length();
			return res;
		}
		int l = i - 1;
		start += l;
		return value.substring( start - l, start );
	}

}
