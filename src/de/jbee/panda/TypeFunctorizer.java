package de.jbee.panda;

/**
 * Able to functorize one kind of value. In most cases this is a single type. In some cases it is a
 * family of types.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface TypeFunctorizer {

	/**
	 * name of the default {@link TypeFunctorizer}. That is the one used when determination by type
	 * or name failed.
	 */
	String DEFAULT = "__default__";

	String TEXT = "__text__";

	String NUMBER = "__number__";

	String LIST = "__list__";

	/**
	 * name of the maybe {@link TypeFunctorizer}
	 */
	String MAYBE = "maybe";

	String TRUE = "true";

	String FALSE = "false";

	Functor functorize( Object value, Functorizer f );

	void install( SetupEnv env );
}
