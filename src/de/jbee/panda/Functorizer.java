package de.jbee.panda;

/**
 * Able to {@link #functorize(Object, FunctorizeEnv)} one kind of value. In most cases this is a
 * single type. In some cases it is a family of types. It means to convert from a common type's
 * value to a {@link Functor} representing it.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface Functorizer {

	/**
	 * name of the default {@link Functorizer}. That is the one used when determination by type or
	 * name failed.
	 */
	String DEFAULT = "__default__";

	String OBJECT = "__obj__";

	String TEXT = "__text__";

	String NUMBER = "__number__";

	String LIST = "__list__";

	/*
	 * core flow control names
	 */

	String CASE = "__case__";

	String DEF = "__def__";

	String VAR = "__var__";

	/*
	 * core constants names
	 */

	/**
	 * name of the maybe {@link Functorizer}
	 */
	String MAYBE = "maybe";

	String TRUE = "true";

	String FALSE = "false";

	String NOTHING = "nothing";

	Functor functorize( Object value, FunctorizeEnv env );

	void setup( SetupEnv env );
}
