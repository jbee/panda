package de.jbee.panda.functor;

import static de.jbee.panda.Env.nothing;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessContext;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

/**
 * <pre>
 * case="..." = env.context().define("case", env.functorize().behavior(CASE, ...));
 * </pre>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class CaseFunctor
		implements BehaviouralFunctor {

	public static final TypeFunctorizer FUNCTORIZER = new CaseFunctorizer();

	//OPEN create a CaseNature ? 

	// hier vermischen sich noch 2 dinge: 
	// 1. der state eines blocks hinsichtlich der case auswertungen seiner direkten kinder
	// 2. der block mit dem case selbst um den block über dependency nicht darzustellen
	// die fabrik oder 2. manipuliert 1. wenn ein case-ausdruck ausgewertet wird

	private final String caseExpr;

	CaseFunctor( String expr ) {
		super();
		this.caseExpr = expr;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		env.context().addDependency( var );
	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		env.context().define( var, nothing( env ) );
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		// OPEN what does it mean when a case is invoked ? 
		return env.invoke( env.functorize().value( caseExpr ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return caseExpr;
	}

	/**
	 * This will evaluate the case expression.
	 * 
	 * It can be seen as some kind of lazy evaluation taking place when
	 * {@link ProcessContext#processed(ProcessingEnv)} is being executed whereby
	 * {@link #bind(Var, ProcessingEnv)} is called for all {@link Var} iables. For all dependencies
	 * {@link #is(EvaluationEnv)} will be called to determine if the context is done.
	 */
	@Override
	public boolean is( EvaluationEnv env ) {
		// TODO eval expr
		return false;
	}

	private static final class CaseFunctorizer
			implements TypeFunctorizer {

		CaseFunctorizer() {
			//make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof String ) {
				return new CaseFunctor( (String) value );
			}
			return f.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( CASE, this );
		}

	}
}
