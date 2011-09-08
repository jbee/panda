package de.jbee.panda;

public interface Functor {

	/**
	 * This method is always called for a <code>let</code> expression even in case there is no
	 * selector expression present. The simple reason is that a {@link Functor} might expect a
	 * selector. In such a case he can respond accordingly to the invocation with a none-
	 * {@link Selector}.
	 */
	Functor invoke( Selector sel, Environment env );

}