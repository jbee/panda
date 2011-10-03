package de.jbee.panda.functor;

import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.TypeFunctorizer;

final class StringFunctor
		extends ValueFunctor {

	static final TypeFunctorizer FUNCTORIZER = new StringFunctorizer();

	private final String value;

	StringFunctor( String value ) {
		super();
		this.value = value;
	}

	private Functor a( String value ) {
		return new StringFunctor( value );
	}

	@Override
	public Functor invoke( Accessor expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '=' ) ) {
			String operand = ""; //FIXME read until space
			if ( operand.startsWith( "*" ) ) {
				return env.invoke( a( value.endsWith( operand.substring( 1 ) ), env ), expr );
			}
			if ( operand.endsWith( "*" ) ) {
				return env.invoke( a(
						value.startsWith( operand.substring( 0, operand.length() - 1 ) ), env ),
						expr );
			}
			return env.invoke( a( value.equalsIgnoreCase( operand ), env ), expr );
		}
		if ( expr.after( '{' ) ) {
			int start = expr.index( 0 );
			if ( expr.after( ':' ) ) {
				int end = expr.index( value.length() - 1 );
				return env.invoke( a( value.substring( start, end + 1 ) ), expr.gobbleAll( '}' ) );
			}
			return env.invoke( a( "" + value.charAt( start ) ), expr.gobbleAll( '}' ) );
		}
		return env.invoke( just( value, env ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return value;
	}

	private static final class StringFunctorizer
			implements TypeFunctorizer {

		StringFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof String ) {
				return new StringFunctor( String.valueOf( value ) );
			}
			return f.function( MAYBE, value );
		}

	}
}
