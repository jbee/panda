package de.jbee.panda;

import de.jbee.panda.functor.Functoring;

public interface Functor
		extends TextNature {

	Functor NOTHING = Functoring.NOTHING;
	Functor JUST = Functoring.JUST;
	Functor TRUE = Functoring.TRUE;
	Functor FALSE = Functoring.FALSE;

	/**
	 * This method is always called for a <code>let</code> expression even in case there is no
	 * selector expression present. The simple reason is that a {@link Functor} might expect a
	 * selector. In such a case he can respond accordingly to the invocation with a none-
	 * {@link Accessor}.
	 */
	Functor invoke( Accessor expr, ProcessingEnv env );

	void renderedAs( Var var, ProcessingEnv env );
}
