package de.jbee.panda.functor;

import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;

public class DefaultFunctorizer
		implements Functorizer {

	// hier eine "map": name -> FunctorFactory

	@Override
	public Functor behaviour( String name, Functor f ) {
		// lookup in map
		return Functor.NOTHING;
	}

	@Override
	public Functor value( Object value ) {
		if ( value instanceof Functor ) {
			return (Functor) value;
		}
		// lookup in map
		return Functor.JUST;
	}

}
