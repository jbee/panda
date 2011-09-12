package de.jbee.panda;

public interface SuperFunctorizer {

	// example: each <functor>
	Functor make( String name, Functor f );

	Functor value( Object value );

	//OPEN lists and objects ?
}
