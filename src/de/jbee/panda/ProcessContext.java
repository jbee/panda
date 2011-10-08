package de.jbee.panda;

public interface ProcessContext {

	void bind( Var var, Functor f );

	Functor boundTo( Var var, Functor unbound );

	void rebind( ProcessingEnv env );

	void bind( ProcessingEnv env );

	void addDependency( Var var );

	boolean independent( ProcessingEnv env );

	//results of case evaluations has to be recognized here so that a context can tell how much
	// successful case expressions has been matched before
}
