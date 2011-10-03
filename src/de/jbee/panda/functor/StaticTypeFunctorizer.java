package de.jbee.panda.functor;

import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

public final class StaticTypeFunctorizer
		implements TypeFunctorizer {

	private final Functor functor;

	StaticTypeFunctorizer( Functor functor ) {
		super();
		this.functor = functor;
	}

	@Override
	public Functor functorize( Object value, Functorizer f ) {
		return functor;
	}

	@Override
	public void install( SetupEnv env ) {
		// installed elsewhere
	}

}
