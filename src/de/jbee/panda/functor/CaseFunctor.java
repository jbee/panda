package de.jbee.panda.functor;

import static de.jbee.panda.Env.false_;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Expr;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
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

	public static final Functorizer FUNCTORIZER = new CaseFunctorizer();

	private final String expr;
	private Functor result;

	CaseFunctor( String expr, Functor nothing ) {
		super();
		this.expr = expr;
		this.result = nothing;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		result = env.eval( Expr.expr( expr ) );
		env.context().addDependency( var );
	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		result = false_( env );
	}

	@Override
	public Functor invoke( Expr expr, EvaluationEnv env ) {
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
				return new CaseFunctor( (String) value, env.behaviour( NOTHING, null ) );
			}
			return env.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( CASE, this );
		}

	}
}
