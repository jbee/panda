package de.jbee.panda.functor;

import static de.jbee.panda.TypeFunctorizer.DEF;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;
import de.jbee.panda.TypeFunctorizer;

/**
 * 
 * <pre>
 * def="..." = env.functorize().behavior(DEF, NOTHING).invoke(..., env);
 * </pre>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class DefineFunctor
		implements Functor {

	static final TypeFunctorizer FUNCTORIZER = new ConstantFunctorizer( DEF, new DefineFunctor() );

	private DefineFunctor() {
		// hide
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		String name = expr.name( TypeFunctorizer.NOTHING );
		return env.invoke( env.functorize().behaviour( name, NOTHING ), expr );
	}

	@Override
	public boolean is() {
		return true;
	}

	@Override
	public String text() {
		return TypeFunctorizer.DEF;
	}

}
