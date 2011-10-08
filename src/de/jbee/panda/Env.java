package de.jbee.panda;

public class Env {

	public static void bind( Var var, Functor functor, ProcessingEnv env ) {
		if ( functor instanceof BehavioralFunctor ) {
			( (BehavioralFunctor) functor ).bind( var, env );
		}
	}

	public static void rebind( Var var, Functor functor, ProcessingEnv env ) {
		if ( functor instanceof BehavioralFunctor ) {
			( (BehavioralFunctor) functor ).rebind( var, env );
		}
	}

	public static Functor a( boolean value, EvaluationEnv env ) {
		return env.functorize().value( Boolean.valueOf( value ) );
	}

	public static Functor nothing( EvaluationEnv env ) {
		return just( Functor.NOTHING, env );
	}

	public static Functor just( Object value, EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.MAYBE, value );
	}

	public static Functor yes( EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.TRUE, true );
	}

	public static Functor no( EvaluationEnv env ) {
		return env.functorize().behaviour( TypeFunctorizer.FALSE, false );
	}
}
