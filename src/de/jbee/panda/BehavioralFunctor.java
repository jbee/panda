package de.jbee.panda;

public interface BehavioralFunctor
		extends Functor {

	void bind( Var var, ProcessingEnv env );

	void rebind( Var var, ProcessingEnv env );
}
