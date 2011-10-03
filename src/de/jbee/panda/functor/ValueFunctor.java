package de.jbee.panda.functor;

import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Var;

public abstract class ValueFunctor
		implements Functor {

	protected final Functor a( boolean value, EvaluationEnv env ) {
		return env.functorize().value( Boolean.valueOf( value ) );
	}

	protected final Functor nothing( EvaluationEnv env ) {
		return just( NOTHING, env );
	}

	protected final Functor just( Object value, EvaluationEnv env ) {
		return env.functorize().function( MAYBE, value );
	}

	@Override
	public final void renderedAs( Var var, ProcessingEnv env ) {
		// nothing to do
	}
}
