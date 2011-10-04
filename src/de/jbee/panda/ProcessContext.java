package de.jbee.panda;

public interface ProcessContext {

	void let( Var var, Functor f );

	Functor assignedTo( Var var );

	void renderFrom( int pos );

	boolean isCompleted();
}
