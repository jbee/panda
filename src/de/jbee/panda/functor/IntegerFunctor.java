package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Range;
import de.jbee.panda.Selector;

public class IntegerFunctor
		implements Functor, IntegralNature, PredicateNature {

	private final int value;

	IntegerFunctor( int value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		if ( sel.isNone() ) {
			return this;
		}
		char op = sel.charAt( 0 );
		if ( "+-*/".indexOf( op ) >= 0 ) {
			if ( sel.length() == 1 ) {
				if ( op == '-' ) {
					return new IntegerFunctor( -value );
				}
			}
			int operand = sel.skip( 1 ).parseInt( 0 );
			switch ( op ) {
				case '+':
					return new IntegerFunctor( value + operand );
				case '-':
					return new IntegerFunctor( value - operand );
				case '*':
					return new IntegerFunctor( value * operand );
				case '/':
					return new IntegerFunctor( value / operand );
			}
		}
		if ( sel.startsWithDigit() ) { // that is a test 
			Range r = sel.parseRange( Integer.MIN_VALUE, Integer.MAX_VALUE );
			if ( r.length() == 1 ) {
				return new BooleanFunctor( value == r.min() );
			}
			return new BooleanFunctor( value >= r.min() && value <= r.max() );
		}
		return env.invoke( Functor.NOTHING, sel );
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
