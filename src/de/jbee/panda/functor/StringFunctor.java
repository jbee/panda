package de.jbee.panda.functor;

import static de.jbee.panda.Env.just;
import de.jbee.panda.Env;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Functorizer;

final class StringFunctor
		implements Functor, PredicateNature {

	static final Functorizer FUNCTORIZER = new StringFunctorizer();

	private final String value;

	StringFunctor( String value ) {
		super();
		this.value = value;
	}

	private Functor a( String value ) {
		return new StringFunctor( value );
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '=' ) ) {
			String operand = expr.untilWhitespace();
			if ( operand.startsWith( "*" ) ) {
				return env.invoke( Env.a( value.endsWith( operand.substring( 1 ) ), env ), expr );
			}
			if ( operand.endsWith( "*" ) ) {
				return env.invoke( Env.a( value.startsWith( operand.substring( 0,
						operand.length() - 1 ) ), env ), expr );
			}
			return env.invoke( Env.a( value.equalsIgnoreCase( operand ), env ), expr );
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
	public String text() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	private static final class StringFunctorizer
			implements Functorizer {

		StringFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value instanceof String ) {
				return new StringFunctor( String.valueOf( value ) );
			}
			if ( value instanceof CharSequence ) {
				return new StringFunctor( value.toString() );
			}
			if ( value instanceof char[] ) {
				return new StringFunctor( String.valueOf( (char[]) value ) );
			}
			return env.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( String.class, this );
			env.install( char[].class, this );
			env.install( TEXT, this );
		}

	}

	@Override
	public boolean is() {
		return value != null && !value.isEmpty();
	}
}
