package de.jbee.panda.functor;

import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

public final class ConstantFunctorizer
		implements TypeFunctorizer {

	private final String name;
	private final Functor functor;

	ConstantFunctorizer( String name, Functor functor ) {
		super();
		this.name = name;
		this.functor = functor;
	}

	@Override
	public Functor functorize( Object value, Functorizer f ) {
		return functor;
	}

	@Override
	public void setup( SetupEnv env ) {
		env.install( name, this );
	}

}
