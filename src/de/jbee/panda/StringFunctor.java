package de.jbee.panda;

public final class StringFunctor
		implements Functor, TextNature {

	private final String value;

	StringFunctor( String value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		if ( sel.length() == 0 ) {
			return this;
		}
		if ( sel.startsWith( "{" ) ) {
			if ( sel.charAt( 1 ) == '.' ) {
				return new StringFunctor( value.substring( 0, sel.skip( 2 ).parseInt(
						value.length() ) ) );
			}
			//TODO others
		}
		return this;
	}

	@Override
	public String text( Environment env ) {
		return value;
	}

}