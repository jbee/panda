package de.jbee.panda;

public final class StringFunctor
		implements Functor {

	private final String value;

	StringFunctor( String value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Instruction arg, ExecutionEnv env ) {
		if ( arg.length() == 0 ) {
			return this;
		}
		if ( arg.startsWith( "{" ) ) {
			if ( arg.charAt( 1 ) == '.' ) {
				return new StringFunctor( value.substring( 0, arg.skip( 2 ).parseInt(
						value.length() ) ) );
			}
			//TODO others
		}
		return this;
	}

	@Override
	public String value( ExecutionEnv nev ) {
		return value;
	}

}
