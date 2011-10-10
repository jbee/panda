package de.jbee.panda.functor;

import static de.jbee.panda.TypeFunctorizer.VAR;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

public class VarFunctor
		implements Functor {

	static final TypeFunctorizer FUNCTORIZER = new ConstantFunctorizer( VAR, new VarFunctor() );

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
			env.define( Var.named( name ), eval( expr, env ) );
		}
		return this;
	}

	private Functor eval( Selector expr, EvaluationEnv env ) {
		if ( expr.after( '@' ) ) {
			return env.invoke( env.value( Var.named( expr.name( "" ) ) ), expr );
		}
		if ( expr.after( '\'' ) ) {
			String constant = expr.until( '\'' );
			return env.invoke( env.functorize().value( constant ), expr.gobble( '\'' ) );
		}
		//TODO numbers
		if ( expr.after( '[' ) ) {
			//TODO
		}
		return env.invoke( env.functorize().behaviour( expr.name( "" ), eval( expr, env ) ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return TypeFunctorizer.VAR;
	}

	@Override
	public boolean is( EvaluationEnv env ) {
		return true;
	}

}
