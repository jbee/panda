package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;

final class BooleanFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature {

	static final Functor TRUE_INSTANCE = new BooleanFunctor( true );
	static final Functor FALSE_INSTANCE = new BooleanFunctor( false );

	private final boolean value;

	private BooleanFunctor( boolean value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.after( '!' ) ) {
			return env.invoke( Functoring.a( !value ), arg );
		}
		//TODO support more logic ops
		return env.invoke( NOTHING, arg );
	}

	@Override
	public String text( Environment env ) {
		return String.valueOf( value );
	}

	@Override
	public boolean is( Environment env ) {
		return value;
	}

	@Override
	public int integer( Environment ent ) {
		return value
			? 1
			: 0;
	}

}
