package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Var;

public abstract class ValueFunctor
		implements Functor {

	protected final Functor a( boolean value ) {
		return value
			? TRUE
			: FALSE;
	}

	@Override
	public final void renderedAs( Var var, Environment env ) {
		// nothing to do
	}
}
