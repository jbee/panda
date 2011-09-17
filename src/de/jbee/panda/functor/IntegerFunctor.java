package de.jbee.panda.functor;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.TypeFunctorizer;

final class IntegerFunctor
		extends ValueFunctor
		implements IntegralNature, PredicateNature {

	static final TypeFunctorizer FUNCTORIZER = new IntegerFunctorizer();

	private final int value;

	IntegerFunctor( int value ) {
		super();
		this.value = value;
	}

	private Functor a( int value ) {
		return new IntegerFunctor( value );
	}

	@Override
	public Functor invoke( Accessor expr, ProcessingEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		char op = expr.charAt( 0 );
		if ( "+-*/".indexOf( op ) >= 0 ) {
			expr.gobbleN( 1 );
			if ( expr.isEmpty() ) {
				return op == '-'
					? env.invoke( a( -value ), expr )
					: env.invoke( nothing( env ), expr );
			}
			int operand = expr.index( 0 );
			switch ( op ) {
				case '+':
					return env.invoke( a( value + operand ), expr );
				case '-':
					return env.invoke( a( value - operand ), expr );
				case '*':
					return env.invoke( a( value * operand ), expr );
				case '/':
					return env.invoke( a( value / operand ), expr );
			}
		}
		if ( expr.after( '=' ) ) {
			int min = expr.index( MIN_VALUE );
			if ( expr.after( ':' ) ) {
				return env.invoke( a( value >= min && value <= expr.index( MAX_VALUE ), env ), expr );
			}
			return env.invoke( a( value == min, env ), expr );
		}
		return env.invoke( just( value, env ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return String.valueOf( value );
	}

	@Override
	public int integer( EvaluationEnv env ) {
		return value;
	}

	@Override
	public boolean is( EvaluationEnv env ) {
		return true;
	}

	private static final class IntegerFunctorizer
			implements TypeFunctorizer {

		IntegerFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof Integer ) {
				return new IntegerFunctor( (Integer) value );
			}
			if ( value instanceof Long ) {
				return new IntegerFunctor( ( (Long) value ).intValue() );
			}
			if ( value instanceof Short ) {
				return new IntegerFunctor( (Short) value );
			}
			if ( value instanceof Byte ) {
				return new IntegerFunctor( (Byte) value );
			}
			return f.function( MAYBE, value );
		}

	}
}
