package de.jbee.panda.functor;

import de.jbee.panda.Functor;
import de.jbee.panda.SuperFunctorizer;

public class SimpleFunctorProducer
		implements SuperFunctorizer {

	// hier eine "map": name -> FunctorFactory

	@Override
	public Functor make( String name, Functor f ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Functor value( Object value ) {
		if ( value == null ) {
			return Functor.NOTHING;
		}
		Class<? extends Object> type = value.getClass();
		if ( type == Integer.class ) {
			return new IntegerFunctor( (Integer) value );
		}
		if ( type == Boolean.class ) {
			return value == Boolean.TRUE
				? Functor.TRUE
				: Functor.FALSE;
		}
		if ( type == String.class ) {
			return new StringFunctor( (String) value );
		}
		return Functor.JUST;
	}

}
