package de.jbee.panda;

public final class Selector
		implements CharSequence {

	public static final char OBJECT = '~';

	public static final Selector EMPTY = new Selector( "", 0 );

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
		return isEmpty()
			? ""
			: value.substring( start );
	}

	public static Selector of( String value ) {
		return new Selector( value, 0 );
	}

	public Selector fork() {
		return new Selector( value, start );
	}

	public boolean isEmpty() {
		return length() <= 0;
	}

	public boolean startsWith( char prefix ) {
		return isEmpty()
			? false
			: charAt( 0 ) == prefix;
	}

	public boolean startsWith( String prefix ) {
		return isEmpty()
			? false
			: value.startsWith( prefix, start );
	}

	public Selector gobble( String prefix ) {
		if ( startsWith( prefix ) ) {
			start += prefix.length();
		}
		return this;
	}

	public Selector gobble( char prefix ) {
		if ( startsWith( prefix ) ) {
			start++;
		}
		return this;
	}

	public Selector gobbleAll( char c ) {
		if ( length() < 0 ) {
			return this;
		}
		final int l = value.length();
		while ( start < l && value.charAt( start ) == c ) {
			start++;
		}
		return this;
	}

	public Selector gobbleN( int length ) {
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

	public String name( String defaultProperty ) {
		if ( !Character.isJavaIdentifierPart( charAt( 0 ) ) ) {
			return defaultProperty;
		}
		StringBuilder b = new StringBuilder();
		int i = 0;
		while ( i < length() && Character.isJavaIdentifierPart( charAt( i ) ) ) {
			b.append( charAt( i ) );
			i++;
		}
		return proceed( b.length() );
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
		return proceed( i - 1 );
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
		return proceed( i - 1 );
	}

	private String proceed( int n ) {
		if ( n <= 0 ) {
			return value.substring( start );
		}
		start += n;
		return value.substring( start - n, start );
	}

	public void gobbleWhitespace() {
		int i = 0;
		while ( i < length() && Character.isWhitespace( charAt( i ) ) ) {
			i++;
		}
		if ( i > 0 ) {
			start += i - 1;
		}
	}

}
