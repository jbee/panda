package de.jbee.panda;

public interface Environment {

	Functor access( Var var );

	Functor invoke( Functor f, Selector arg );

	void let( Var var, Functor f );

	void renderFrom( int pos );
}
