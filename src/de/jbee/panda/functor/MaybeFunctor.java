package de.jbee.panda.functor;

import static de.jbee.panda.Env.false_;
import static de.jbee.panda.Env.nothing;
import static de.jbee.panda.Env.true_;
import de.jbee.lang.List;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;

abstract class MaybeFunctor
		implements Functor {

	static final Functorizer FUNCTORIZER = new MaybeFunctorizer();

	static final Functor NOTHING_INSTANCE = new NothingFunctor();

	private static final class JustFunctor
			extends MaybeFunctor
			implements IntegralNature {

		private final Object value;

		JustFunctor( Object value ) {
			super();
			this.value = value;
		}

		@Override
		public boolean is() {
			return value instanceof Boolean
				? value == Boolean.TRUE
				: value instanceof PredicateNature
					? ( (PredicateNature) value ).is()
					: true;
		}

		@Override
		public Functor invoke( Selector expr, EvaluationEnv env ) {
			if ( expr.after( '?' ) ) {
				return env.invoke( true_( env ), expr );
			}
			return env.invoke( nothing( env ), expr );
		}

		@Override
		public String text() {
			return String.valueOf( value );
		}

		@Override
		public int integer() {
			return value instanceof Number
				? ( (Number) value ).intValue()
				: 1;
		}

		@Override
		public String toString() {
			return String.valueOf( value ) + "?";
		}

	}

	private static final class NothingFunctor
			extends MaybeFunctor
			implements IntegralNature, ListNature {

		NothingFunctor() {
			// hide
		}

		@Override
		public Functor invoke( Selector expr, EvaluationEnv env ) {
			if ( expr.after( '?' ) ) {
				return env.invoke( false_( env ), expr );
			}
			// OPEN tell env about invocation on nothing ?
			return this;
		}

		@Override
		public boolean is() {
			return false;
		}

		@Override
		public int integer() {
			return 0;
		}

		@Override
		public String text() {
			return "";
		}

		@Override
		public List<Functor> elements() {
			return List.with.noElements();
		}

		@Override
		public String toString() {
			return "";
		}
	}

	private static final class ExistsFunctorizer
			implements Functorizer {

		ExistsFunctorizer() {
			// make accessible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			return env.value( env.value( value ) != NOTHING_INSTANCE );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( "exists", this );
		}

	}

	private static final class NotFunctorizer
			implements Functorizer {

		NotFunctorizer() {
			// make accessible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			// very important: NOT nothing is  still nothing
			Functor negated = env.value( value );
			return negated == NOTHING_INSTANCE
				? NOTHING_INSTANCE
				: env.value( !negated.is() );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( "not", this );
		}

	}

	private static final class MaybeFunctorizer
			implements Functorizer {

		static final Functorizer NOT_FUNCTORIZER = new NotFunctorizer();
		static final Functorizer EXISTS_FUNCTORIZER = new ExistsFunctorizer();

		MaybeFunctorizer() {
			// make accessible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			return value == Functor.NOTHING
				? NOTHING_INSTANCE
				: new JustFunctor( value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( Void.class, this );
			env.install( MAYBE, this );
			// functions used as constants
			env.install( NOTHING, NOTHING_INSTANCE );
			NOT_FUNCTORIZER.setup( env );
			EXISTS_FUNCTORIZER.setup( env );
		}

	}
}
