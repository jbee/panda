package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;

final class IntegerFunctor
		extends ValueFunctor
		implements IntegralNature, PredicateNature {

	private final int value;

	IntegerFunctor( int value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.isNone() ) {
			return this;
		}
		char op = arg.charAt( 0 );
		if ( "+-*/".indexOf( op ) >= 0 ) {
			arg.skipNext( 1 );
			if ( arg.isNone() ) {
				return op == '-'
					? env.invoke( Functoring.a( -value ), arg )
					: env.invoke( Functor.NOTHING, arg );
			}
			int operand = arg.integer( 0 );
			switch ( op ) {
				case '+':
					return env.invoke( Functoring.a( value + operand ), arg );
				case '-':
					return env.invoke( Functoring.a( value - operand ), arg );
				case '*':
					return env.invoke( Functoring.a( value * operand ), arg );
				case '/':
					return env.invoke( Functoring.a( value / operand ), arg );
			}
		}
		if ( arg.after( '?' ) ) { // that is a test 
			int min = arg.integer( Integer.MIN_VALUE );
			if ( arg.after( ".." ) ) {
				return env.invoke( Functoring.a( value >= min
						&& value <= arg.integer( Integer.MAX_VALUE ) ), arg );
			}
			return env.invoke( Functoring.a( value == min ), arg );
		}
		return env.invoke( Functor.NOTHING, arg );
	}

	@Override
	public String text( Environment env ) {
		return String.valueOf( value );
	}

	@Override
	public int integer( Environment ent ) {
		return value;
	}

	@Override
	public boolean is( Environment env ) {
		return value != 0;
	}
}
