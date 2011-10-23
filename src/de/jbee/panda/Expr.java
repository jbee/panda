package de.jbee.panda;

public final class Expr
		implements CharSequence {

	public static final char OBJECT = '~';

	public static final Expr EMPTY = new Expr( "", 0, 0 );

	private final String value;
	private int start;
	private int end;

	private Expr( String value, int start, int end ) {
		super();
		this.value = value;
		this.start = Math.min( start, value.length() );
		this.end = Math.min( end, value.length() - start );
	}

	public static Expr ref( String var ) {
		return expr( "@" + var );
	}

	public Expr join( Expr sub ) {
		return new Expr( plain() + sub.plain(), 0, 0 );
	}

	@Override
	public String toString() {
		return plain();
	}

	public String plain() {
		return isEmpty()
			? ""
			: value.substring( start );
	}

	public static Expr expr( String value ) {
		return new Expr( value, 0, 0 );
	}

	public Expr fork() {
		return new Expr( value, start, end );
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

	public Expr gobble( String prefix ) {
		if ( startsWith( prefix ) ) {
			start += prefix.length();
		}
		return this;
	}

	public Expr gobble( char prefix ) {
		if ( startsWith( prefix ) ) {
			start++;
		}
		return this;
	}

	public Expr gobbleAll( char c ) {
		if ( length() < 0 ) {
			return this;
		}
		final int l = value.length();
		while ( start < l && value.charAt( start ) == c ) {
			start++;
		}
		return this;
	}

	public Expr gobbleN( int length ) {
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
		return index < 0
			? value.charAt( value.length() - index - end )
			: value.charAt( start + index );
	}

	@Override
	public int length() {
		return value.length() - start - end;
	}

	@Override
	public CharSequence subSequence( int start, int end ) {
		return new Expr( value.substring( start + start, end ), 0, 0 );
	}

	public static Expr elemAt( int index ) {
		return expr( "[" + index + "]" );
	}

	public boolean after( String prefix ) {
		return incIf( prefix.length(), startsWith( prefix ) );
	}

	public boolean after( char c ) {
		return incIf( 1, startsWith( c ) );
	}

	public boolean before( char c ) {
		if ( charAt( -1 ) == c ) {
			end++;
			return true;
		}
		return false;
	}

	private boolean incIf( int inc, boolean cond ) {
		if ( cond ) {
			start += inc;
		}
		return cond;
	}

	public static Expr range( int start, int end ) {
		return expr( "[" + start + ":" + end + "]" );
	}

	public String name( String defaultProperty ) {
		if ( isEmpty() ) {
			return defaultProperty;
		}
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

	@Override
	public int hashCode() {
		return plain().hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof Expr ) {
			return ( (Expr) obj ).plain().equals( plain() );
		}
		return false;
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

	public Expr untilWhitespace() {
		int i = 0;
		while ( i < length() && !Character.isWhitespace( charAt( i ) ) ) {
			i++;
		}
		if ( i == 0 ) {
			return EMPTY;
		}
		if ( i >= length() ) {
			Expr res = fork();
			start += length();
			return res;
		}
		return expr( proceed( i ) );
	}

	private String proceed( int n ) {
		if ( n <= 0 ) {
			return value.substring( start );
		}
		start += n;
		return value.substring( start - n, start );
	}

	public Expr gobbleWhitespace() {
		int i = 0;
		while ( i < length() && Character.isWhitespace( charAt( i ) ) ) {
			i++;
		}
		if ( i > 0 ) {
			start += i;
		}
		return this; // chaining
	}

}
