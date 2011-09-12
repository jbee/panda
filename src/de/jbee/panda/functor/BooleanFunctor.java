package de.jbee.panda.functor;

import de.jbee.panda.Accessor;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.SuperFunctorizer;
import de.jbee.panda.Functorizer;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;

final class BooleanFunctor
		extends ValueFunctor
		implements PredicateNature, IntegralNature {

	static final Functor TRUE_INSTANCE = new BooleanFunctor( true );
	static final Functor FALSE_INSTANCE = new BooleanFunctor( false );

	// should be defined after true and false instance!
	static final Functorizer FUNCTORIZER = new BooleanFunctorizer();

	private final boolean value;

	private BooleanFunctor( boolean value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Accessor expr, Environment env ) {
		if ( expr.after( '!' ) ) {
			return env.invoke( Functoring.a( !value ), expr );
		}
		if ( expr.after( '=' ) ) {
			return expr.startsWith( "true" )
				? env.invoke( TRUE, expr.gobble( "true" ) )
				: env.invoke( FALSE, expr.gobble( "false" ) );
		}
		return env.invoke( JUST, expr );
	}

	@Override
	public String text( Environment env ) {
		return String.valueOf( value );
	}

	@Override
	public boolean is( Environment env ) {
		return value;
	}

	@Override
	public int integer( Environment ent ) {
		return value
			? 1
			: 0;
	}

	private static final class BooleanFunctorizer
			implements Functorizer {

		BooleanFunctorizer() {
			super(); //make visible
		}

		@Override
		public Functor functorize( Object value, SuperFunctorizer sf ) {
			if ( value instanceof Boolean ) {
				return ( (Boolean) value ).booleanValue()
					? TRUE
					: FALSE;
			}
			return NOTHING;
		}

	}
}
