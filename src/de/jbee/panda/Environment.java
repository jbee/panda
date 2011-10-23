package de.jbee.panda;

import static de.jbee.panda.Env.nothing;

import java.util.Deque;
import java.util.LinkedList;

import de.jbee.panda.functor.Functorize;

public class Environment
		implements ProcessingEnv {

	private final Deque<ProcessContext> contextStack = new LinkedList<ProcessContext>();

	public Environment( ProcessContext context ) {
		contextStack.push( context ); // the master context
	}

	@Override
	public ProcessContext context() {
		return contextStack.peek();
	}

	@Override
	public Functor value( Var var ) {
		if ( !var.isUndefined() ) {
			for ( ProcessContext c : contextStack ) {
				Functor f = c.definedAs( var, null );
				if ( f != null ) {
					return f;
				}
			}
		}
		return functorize().value( Functor.NOTHING );
	}

	@Override
	public FunctorizeEnv functorize() {
		return Functorize.getInstance();
	}

	@Override
	public Functor invoke( Functor f, Expr expr ) {
		return f.invoke( expr, this );
	}

	@Override
	public void open( ProcessContext context ) {
		contextStack.push( context );
	}

	@Override
	public void close( ProcessContext context ) {
		while ( contextStack.size() > 1 && contextStack.peek() != context ) {
			contextStack.pop();
		}
		if ( contextStack.size() > 1 && contextStack.peek() == context ) {
			contextStack.pop();
		}
	}

	@Override
	public void define( Var var, Functor f ) {
		contextStack.peek().define( var, f );
	}

	@Override
	public Functor eval( Expr expr ) {
		expr.gobbleWhitespace();
		if ( expr.isEmpty() ) {
			return nothing( this );
		}
		if ( expr.after( '@' ) ) {
			return invoke( value( Var.named( expr.name( "" ) ) ), expr );
		}
		if ( expr.after( '\'' ) ) { // string constants
			return invoke( functorize().value( expr.until( '\'' ) ), expr.gobble( '\'' ) );
		}
		//TODO numbers
		if ( expr.after( '[' ) ) { // list constants
			//TODO
		}
		String name = expr.name( "" );
		if ( name.isEmpty() ) {
			return invoke( nothing( this ), expr );
		}
		Expr selector = expr.untilWhitespace();
		return invoke( functorize().behaviour( name, eval( expr ) ), selector );
	}

}
