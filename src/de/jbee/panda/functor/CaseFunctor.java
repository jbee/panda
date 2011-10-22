package de.jbee.panda.functor;

import static de.jbee.panda.Env.nothing;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
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

	private static final Var CASE_VAR = Var.named( "__case__" );

	public static final Functorizer FUNCTORIZER = new CaseFunctorizer();

	private final String expr;
	private Functor result;

	CaseFunctor( String expr ) {
		super();
		this.expr = expr;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		result = env.eval( Selector.of( expr ) );
		env.define( CASE_VAR, result );
		env.context().addDependency( CASE_VAR );
	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		env.context().define( CASE_VAR, nothing( env ) ); // in any case just once
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		// OPEN what does it mean when a case is invoked ? 
		return env.invoke( result, expr );
	}

	@Override
	public String text() {
		return String.valueOf( result );
	}

	@Override
	public boolean is() {
		return result.is();
	}

	private static final class CaseFunctorizer
			implements Functorizer {

		CaseFunctorizer() {
			//make visible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value instanceof String ) {
				return new CaseFunctor( (String) value );
			}
			return env.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( CASE, this );
		}

	}
}
