package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Selector;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

abstract class MaybeFunctor
		extends ValueFunctor {

	static final TypeFunctorizer FUNCTORIZER = new MaybeFunctorizer();

	static final Functor NOTHING_INSTANCE = new NothingFunctor();

	private static final class JustFunctor
			extends MaybeFunctor
			implements PredicateNature, IntegralNature {

		private final Object value;

		JustFunctor( Object value ) {
			super();
			this.value = value;
		}

		@Override
		public boolean is( EvaluationEnv env ) {
			return value instanceof Boolean
				? value == Boolean.TRUE
				: true;
		}

		@Override
		public Functor invoke( Selector expr, EvaluationEnv env ) {
			if ( expr.after( '?' ) ) {
				return env.invoke( a( true, env ), expr );
			}
			return env.invoke( nothing( env ), expr );
		}

		@Override
		public String text( EvaluationEnv env ) {
			return String.valueOf( value );
		}

		@Override
		public int integer( EvaluationEnv env ) {
			return value instanceof Number
				? ( (Number) value ).intValue()
				: 1;
		}

	}

	private static final class NothingFunctor
			extends MaybeFunctor
			implements PredicateNature, IntegralNature, ListNature {

		NothingFunctor() {
			// hide
		}

		@Override
		public Functor invoke( Selector expr, EvaluationEnv env ) {
			if ( expr.after( '?' ) ) {
				return env.invoke( a( false, env ), expr );
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
	}

	private static final class MaybeFunctorizer
			implements TypeFunctorizer {

		MaybeFunctorizer() {
			// make accessible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			return value == Functor.NOTHING
				? NOTHING_INSTANCE
				: new JustFunctor( value );
		}

		@Override
		public void install( SetupEnv env ) {
			env.install( Void.class, this );
			env.install( NOTHING, this );
			env.install( MAYBE, this );
		}

	}
}
