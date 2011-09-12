package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Accessor;

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
	public Functor invoke( Accessor expr, Environment env ) {
		if ( expr.after( '?' ) ) {
			return env.invoke( TRUE, expr );
		}
		return env.invoke( NOTHING, expr );
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
