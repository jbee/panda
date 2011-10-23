package de.jbee.panda.functor;

import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Expr;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Var;

public class VarFunctor
		implements BehaviouralFunctor {

	static final Functorizer FUNCTORIZER = new VarFunctorizer();

	private final String stmt;

	VarFunctor( String stmt ) {
		this.stmt = stmt;
	}

	@Override
	public String toString() {
		return "var: " + stmt;
	}

	@Override
	public Functor invoke( Expr expr, EvaluationEnv env ) {

		return this;
	}

	@Override
	public String text() {
		return stmt;
	}

	@Override
	public boolean is() {
		return true;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		Expr expr = Expr.expr( stmt );
		expr.gobble( '@' );
		String name = expr.name( null );
		if ( name == null ) {
			//TODO warn about wrong useage of var functor expr
			return;
		}
		if ( name.startsWith( "__" ) && name.endsWith( "__" ) ) {
			//TODO warning about an attempt to redefine core concept - or is it up to the env to check that ? -> all names like __xyz__ are not allowed to be redefined !?
			return;
		}
		expr.gobbleWhitespace();
		if ( expr.after( "as" ) ) {
			expr.gobbleWhitespace();
			env.define( Var.named( name ), env.eval( expr ) );
		}
	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		// nothing to do
	}

	private static final class VarFunctorizer
			implements Functorizer {

		VarFunctorizer() {
			// make visible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value instanceof String ) {
				return new VarFunctor( (String) value );
			}
			return env.behaviour( MAYBE, value );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( VAR, this );
		}

	}

}
