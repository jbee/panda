package de.jbee.panda.functor;

import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.TypeFunctorizer;

final class JustFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature {

	public static final Functor INSTANCE = new JustFunctor();
	static final TypeFunctorizer FUNCTORIZER = new JustFunctorizer();

	private JustFunctor() {
		// hide
	}

	@Override
	public boolean is( EvaluationEnv env ) {
		return true;
	}

	@Override
	public Functor invoke( Accessor expr, ProcessingEnv env ) {
		if ( expr.after( '?' ) ) {
			return env.invoke( TRUE, expr );
		}
		return env.invoke( NOTHING, expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return "*";
	}

	@Override
	public int integer( EvaluationEnv env ) {
		return 1;
	}

	private static final class JustFunctorizer
			implements TypeFunctorizer {

		JustFunctorizer() {
			//visibility
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			return Functor.JUST;
		}

	}
}
