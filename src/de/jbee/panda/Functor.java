package de.jbee.panda;

public interface Functor
		extends TextNature {

	Object NOTHING = null;

	String MAYBE = "maybe";
	String TRUE = "true";
	String FALSE = "false";

	/**
	 * This method is always called for a <code>let</code> expression even in case there is no
	 * selector expression present. The simple reason is that a {@link Functor} might expect a
	 * selector. In such a case he can respond accordingly to the invocation with a none-
	 * {@link Accessor}.
	 */
	Functor invoke( Accessor expr, EvaluationEnv env );

	void renderedAs( Var var, ProcessingEnv env );
}
