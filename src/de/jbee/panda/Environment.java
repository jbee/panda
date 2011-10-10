package de.jbee.panda;

import java.util.Deque;
import java.util.LinkedList;

import de.jbee.panda.functor.DefaultFunctorizer;

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
	public Functorizer functorize() {
		return DefaultFunctorizer.getInstance();
	}

	@Override
	public Functor invoke( Functor f, Selector expr ) {
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

}
