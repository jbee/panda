package de.jbee.panda;

public interface Environment {

	Functor access( Var var );

	void let( Var var, Functor f );

	Functor invoke( Functor f, Selector s );
}
