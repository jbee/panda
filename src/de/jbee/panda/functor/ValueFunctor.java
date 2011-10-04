package de.jbee.panda.functor;

import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

public abstract class ValueFunctor
		implements Functor {

	protected final Functor a( boolean value, EvaluationEnv env ) {
		return env.functorize().value( Boolean.valueOf( value ) );
	}

	protected final Functor yes( EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.TRUE, true );
	}

	protected final Functor no( EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.FALSE, false );
	}

	protected final Functor nothing( EvaluationEnv env ) {
		return just( NOTHING, env );
	}

	protected final Functor just( Object value, EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.MAYBE, value );
	}

	@Override
	public void processedAs( Var var, ProcessingEnv env ) {
		// default: nothing to do
	}
}
