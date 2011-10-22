package de.jbee.panda;

public interface ProcessContext {

	void define( Var var, Functor f );

	Functor definedAs( Var var, Functor undefined );

	void addDependency( Var var );

	boolean processed( ProcessingEnv env );

	//results of case evaluations has to be recognized here so that a context can tell how much
	// successful case expressions has been matched before
}
