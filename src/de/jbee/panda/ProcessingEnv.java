package de.jbee.panda;

public interface ProcessingEnv
		extends EvaluationEnv {

	void let( Var var, Functor f );

	void renderFrom( int pos );

}
