package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Functor;

public class Functoring {

	public static final Functor NOTHING = NothingFunctor.INSTANCE;
	public static final Functor JUST = JustFunctor.INSTANCE;
	public static final Functor TRUE = BooleanFunctor.TRUE_INSTANCE;
	public static final Functor FALSE = BooleanFunctor.FALSE_INSTANCE;

	public static Functor a( boolean value ) {
		return value
			? Functor.TRUE
			: Functor.FALSE;
	}

	public static Functor a( int value ) {
		return new IntegerFunctor( value );
	}

	public static Functor a( String value ) {
		return new StringFunctor( value );
	}

	public static Functor a( List<Functor> elems ) {
		return new ListFunctor( elems );
	}
}
