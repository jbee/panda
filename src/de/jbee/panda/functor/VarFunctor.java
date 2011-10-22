package de.jbee.panda.functor;

import static de.jbee.panda.Functorizer.VAR;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Selector;
import de.jbee.panda.Var;

public class VarFunctor
		implements BehaviouralFunctor {

	static final Functorizer FUNCTORIZER = new ConstantFunctorizer( VAR, new VarFunctor() );

	private VarFunctor() {
		// hide
	}

	@Override
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		expr.gobble( '@' );
		String name = expr.name( null );
		if ( name == null ) {
			//TODO warn about wrong useage of var functor expr
			return this;
		}
		if ( name.startsWith( "__" ) && name.endsWith( "__" ) ) {
			//TODO warning about an attempt to redefine core concept - or is it up to the env to check that ? -> all names like __xyz__ are not allowed to be redefined !?
			return this;
		}
		expr.gobbleWhitespace();
		if ( expr.after( "as" ) ) {
			expr.gobbleWhitespace();
			env.define( Var.named( name ), env.eval( expr ) );
		}
		return this;
	}

	@Override
	public String text() {
		return Functorizer.VAR;
	}

	@Override
	public boolean is() {
		return true;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		// TODO Auto-generated method stub

	}

}
