package de.jbee.panda;

/**
 * Able to functorize one kind of value. In most cases this is a single type. In some cases it is a
 * family of types.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface TypeFunctorizer {

	Functor functorize( Object value, Functorizer f );
}
