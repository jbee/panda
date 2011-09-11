package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;

final class StringFunctor
		extends ValueFunctor {

	private final String value;

	StringFunctor( String value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.isNone() ) {
			return this;
		}
		if ( arg.after( '?' ) ) {
			//TODO value comparison things
			return Functor.TRUE;
		}
		if ( arg.after( '{' ) ) {
			int start = arg.integer( 0 );
			if ( arg.after( ".." ) ) {
				int end = arg.integer( value.length() - 1 );
				return env.invoke( Functoring.a( value.substring( start, end + 1 ) ),
						arg.skip( '}' ) );
			}
			return env.invoke( Functoring.a( value.charAt( start ) ), arg.skip( '}' ) );
		}
		return env.invoke( Functor.NOTHING, arg );
	}

	@Override
	public String text( Environment env ) {
		return value;
	}

}
