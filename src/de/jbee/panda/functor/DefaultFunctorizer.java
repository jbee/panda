package de.jbee.panda.functor;

import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.TypeFunctorizer;

public class DefaultFunctorizer
		implements Functorizer {

	// hier eine "map": name -> FunctorFactory

	@Override
	public Functor behaviour( String name, Functor f ) {
		return get( name, MaybeFunctor.FUNCTORIZER ).functorize( f, this );
	}

	@Override
	public Functor value( Object value ) {
		if ( value instanceof Functor ) {
			return (Functor) value;
		}
		return get( value.getClass().getCanonicalName(), MaybeFunctor.FUNCTORIZER ).functorize(
				value, this );
	}

	@Override
	public Functor function( String name, Object value ) {
		return get( name, MaybeFunctor.FUNCTORIZER ).functorize( value, this );
	}

	private TypeFunctorizer get( String name, TypeFunctorizer fallback ) {

		return fallback;
	}
}