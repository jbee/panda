package de.jbee.panda;

public interface Functor
		extends TextNature, PredicateNature {

	/**
	 * The value that means nothing
	 */
	Object NOTHING = null;

	/**
	 * This method is always called for a <code>let</code> expression even in case there is no
	 * selector expression present. The simple reason is that a {@link Functor} might expect a
	 * selector. In such a case he can respond accordingly to the invocation with a none-
	 * {@link Expr}.
	 */
	Functor invoke( Expr expr, EvaluationEnv env );

}
