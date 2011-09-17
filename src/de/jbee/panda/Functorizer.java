package de.jbee.panda;

public interface Functorizer {

	/**
	 * Boxes the functor <code>f</code> into another behavioral functor identified by
	 * <code>name</code>. It is used to resolve the {@linkplain Functor} bound to a new {@link Var}
	 * inside a <code>let</code> expressions.
	 * 
	 * The <code>each</code> functor is an example.
	 */
	Functor behaviour( String name, Functor f );

	/**
	 * Wraps the value into a functor. The {@link Functor} chosen is resolved by the values type.
	 */
	Functor value( Object value );

	/**
	 * Boxes the value by a formatting functor named <code>name</code>.
	 */
	Functor function( String name, Object value );

}
