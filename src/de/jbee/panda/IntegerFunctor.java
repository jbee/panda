package de.jbee.panda;

public class IntegerFunctor
		implements Functor {

	private final int value;

	IntegerFunctor( int value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector sel, ExecutionEnv env ) {
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
		return this;
	}

	@Override
	public String value( ExecutionEnv env ) {
		return String.valueOf( value );
	}

}
