package de.jbee.panda;

public interface BehaviouralFunctor
		extends Functor {

	void bind( Var var, ProcessingEnv env );

	void rebind( Var var, ProcessingEnv env );
}
