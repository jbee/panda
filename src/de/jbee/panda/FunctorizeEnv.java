package de.jbee.panda;

public interface FunctorizeEnv {

	/**
	 * Expands the <code>value</code> into a functor. The {@link Functor} chosen is resolved by the
	 * value's type.
	 */
	Functor value( Object value );

	/**
	 * Boxes the <code>value</code> into a behavioral functor identified by <code>name</code>.
	 * 
	 * This is used e.g. to resolve the {@linkplain Functor} bound to a new {@link Var} inside a
	 * <code>let</code> expressions. The <code>each</code> functor is an example.
	 * 
	 * Note that the value might already be a {@linkplain Functor}. This doesn't make a difference.
	 */
	Functor behaviour( String name, Object value );

}
