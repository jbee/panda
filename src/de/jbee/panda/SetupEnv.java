package de.jbee.panda;

public interface SetupEnv {

	void install( String name, Functor constant );

	void install( String name, Functorizer functorizer );

	void install( Class<?> type, Functorizer functorizer );
}
