package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.TypeFunctorizer;

final class NothingFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature, ListNature {

	static final TypeFunctorizer FUNCTORIZER = new NothingFunctorizer();

	public static final Functor INSTANCE = new NothingFunctor();

	private NothingFunctor() {
		// hide
	}

	@Override
	public Functor invoke( Accessor expr, ProcessingEnv env ) {
		if ( expr.after( '?' ) ) {
			return env.invoke( FALSE, expr );
		}
		// OPEN tell env about invocation on nothing ?
		return this;
	}

	@Override
	public boolean is( EvaluationEnv env ) {
		return false;
	}

	@Override
	public int integer( EvaluationEnv env ) {
		return 0;
	}

	@Override
	public String text( EvaluationEnv env ) {
		return "";
	}

	@Override
	public List<Functor> elements( EvaluationEnv env ) {
		return List.with.noElements();
	}

	private static final class NothingFunctorizer
			implements TypeFunctorizer {

		NothingFunctorizer() {
			// visibility
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			return Functor.NOTHING;
		}

	}
}
