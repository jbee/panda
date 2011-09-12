package de.jbee.panda;

public interface Environment
		extends CaseEnv {

	void let( Var var, Functor f );

	void renderFrom( int pos );

	SuperFunctorizer functorize();
}
