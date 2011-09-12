package de.jbee.panda;

public interface CaseEnv {

	Functor access( Var var );

	Functor invoke( Functor f, Accessor expr );
}
