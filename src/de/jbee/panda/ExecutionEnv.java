package de.jbee.panda;

public interface ExecutionEnv {

	Functor functorFor( Alias a );

	void let( Alias a, Functor f );

	Functor invoke( Functor f, Selector s );
}
