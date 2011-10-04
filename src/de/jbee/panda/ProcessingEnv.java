package de.jbee.panda;

public interface ProcessingEnv
		extends EvaluationEnv {

	ProcessContext context();

	void open( ProcessContext context );

	void close( ProcessContext context );

}
