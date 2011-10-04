package de.jbee.panda;

/**
 * A stripped down {@link ProcessingEnv} contains the functionality needed to evaluate
 * <code>case</code> expressions and compute the variable values of the content.
 * 
 * Therefore the {@linkplain EvaluationEnv} doesn't provide any method that would change state of
 * the environment itself. It is some kind of a read-only access facade.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface EvaluationEnv {

	Functor access( Var var );

	Functor invoke( Functor f, Selector expr );

	Functorizer functorize();

}
