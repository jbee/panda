package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Var;

public abstract class ValueFunctor
		implements Functor {

	@Override
	public final void bind( Var var, Environment env ) {
		// nothing to do
	}
}
