package de.jbee.panda;

public interface SetupEnv {

	void install( String name, Functor constant );

	void install( String name, TypeFunctorizer functorizer );

	void install( Class<?> type, TypeFunctorizer functorizer );
}
