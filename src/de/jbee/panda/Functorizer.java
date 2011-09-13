package de.jbee.panda;

public interface Functorizer {

	/**
	 * Boxes the functor <code>f</code> into another behavioral functor named <code>name</code>.
	 * 
	 * The <code>each</code> functor is an example.
	 */
	Functor behaviour( String name, Functor f );

	/**
	 * Wraps the value into a functor. The {@link Functor} chosen is resolved by the values type.
	 */
	Functor value( Object value );

}
