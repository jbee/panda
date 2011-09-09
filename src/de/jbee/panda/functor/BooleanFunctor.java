package de.jbee.panda.functor;

import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;

public class BooleanFunctor
		implements Functor, PredicateNature, IntegralNature {

	private final boolean value;

	BooleanFunctor( boolean value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		// TODO Auto-generated method stub
		return this;
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

}
