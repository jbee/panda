package de.jbee.panda.functor;

import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.TypeFunctorizer;

final class BooleanFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature {

	private static final Functor TRUE_INSTANCE = new BooleanFunctor( true );
	private static final Functor FALSE_INSTANCE = new BooleanFunctor( false );

	// should be defined after true and false instance!
	static final TypeFunctorizer FUNCTORIZER = new BooleanFunctorizer();

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
	public Functor invoke( Accessor expr, ProcessingEnv env ) {
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
	public String text( EvaluationEnv env ) {
		return String.valueOf( value );
	}

	@Override
	public boolean is( EvaluationEnv env ) {
		return value;
	}

	@Override
	public int integer( EvaluationEnv env ) {
		return value
			? 1
			: 0;
	}

	private static final class BooleanFunctorizer
			implements TypeFunctorizer {

		BooleanFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof Boolean ) {
				return ( (Boolean) value ).booleanValue()
					? TRUE_INSTANCE
					: FALSE_INSTANCE;
			}
			return f.function( MAYBE, value );
		}

	}
}
