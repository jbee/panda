package de.jbee.panda;

public class IntegerFunctor
		implements Functor {

	private final int value;

	IntegerFunctor( int value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Instruction arg, ExecutionEnv env ) {
		if ( arg.isNone() ) {
			return this;
		}
		char op = arg.charAt( 0 );
		if ( "+-*/".indexOf( op ) >= 0 ) {
			if ( arg.length() == 1 ) {
				if ( op == '-' ) {
					return new IntegerFunctor( -value );
				}
			}
			int operand = arg.skip( 1 ).parseInt( 0 );
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
	public String value( ExecutionEnv nev ) {
		return String.valueOf( value );
	}

}
