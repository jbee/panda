package de.jbee.panda.functor;

import static de.jbee.panda.Env.just;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Functorizer;

final class BooleanFunctor
		implements Functor, PredicateNature, IntegralNature {

	static final Functorizer FUNCTORIZER = new BooleanFunctorizer();

	static final Functor TRUE_INSTANCE = new BooleanFunctor( true );
	static final Functor FALSE_INSTANCE = new BooleanFunctor( false );

	private final boolean value;

	private BooleanFunctor( boolean value ) {
		super();
		this.value = value;
	}

	private Functor a( boolean value ) {
		return value
			? TRUE_INSTANCE
			: FALSE_INSTANCE;
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		if ( expr.after( '!' ) ) {
			return env.invoke( a( !value ), expr );
		}
		if ( expr.after( '=' ) ) {
			return expr.startsWith( "true" )
				? env.invoke( a( value == true ), expr.gobble( "true" ) )
				: env.invoke( a( value == false ), expr.gobble( "false" ) );
		}
		return env.invoke( just( value, env ), expr );
	}

	@Override
	public String text() {
		return String.valueOf( value );
	}

	@Override
	public boolean is() {
		return value;
	}

	@Override
	public int integer() {
		return value
			? 1
			: 0;
	}

	@Override
	public String toString() {
		return String.valueOf( value );
	}

	private static final class BooleanFunctorizer
			implements Functorizer {

		BooleanFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value instanceof Boolean ) {
				return ( (Boolean) value ).booleanValue()
					? TRUE_INSTANCE
					: FALSE_INSTANCE;
			}
			return env.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( boolean.class, this );
			env.install( Boolean.class, this );
			// functions used as constants
			env.install( Functorizer.TRUE, TRUE_INSTANCE );
			env.install( Functorizer.FALSE, FALSE_INSTANCE );
		}

	}
}
