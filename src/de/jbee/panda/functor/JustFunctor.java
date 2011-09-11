package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;

final class JustFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature {

	public static final Functor INSTANCE = new JustFunctor();

	private JustFunctor() {
		// hide
	}

	@Override
	public boolean is( Environment env ) {
		return true;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.after( '?' ) ) {
			return env.invoke( TRUE, arg );
		}
		return env.invoke( NOTHING, arg );
	}

	@Override
	public String text( Environment env ) {
		return "*";
	}

	@Override
	public int integer( Environment ent ) {
		return 1;
	}

}
