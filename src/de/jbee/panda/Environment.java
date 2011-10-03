package de.jbee.panda;

import de.jbee.panda.functor.DefaultFunctorizer;

public class Environment
		implements ProcessingEnv {

	@Override
	public void let( Var var, Functor f ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderFrom( int pos ) {
		// TODO Auto-generated method stub

	}

	@Override
	public Functor access( Var var ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Functorizer functorize() {
		return DefaultFunctorizer.getInstance();
	}

	@Override
	public Functor invoke( Functor f, Accessor expr ) {
		return f.invoke( expr, this );
	}

}
