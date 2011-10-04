package de.jbee.panda;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.jbee.panda.functor.DefaultFunctorizer;

public class Environment
		implements ProcessingEnv {

	private final Deque<ProcessContext> contextStack = new LinkedList<ProcessContext>();

	static class Context
			implements ProcessContext {

		final Map<Var, Functor> vars = new HashMap<Var, Functor>();

		@Override
		public void let( Var var, Functor f ) {
			vars.put( var, f );
		}

		@Override
		public void renderFrom( int pos ) {
			// TODO Auto-generated method stub

		}

		@Override
		public Functor assignedTo( Var var ) {
			return vars.get( var );
		}

		@Override
		public boolean isCompleted() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public Environment() {
		contextStack.push( new Context() ); // the master context
	}

	@Override
	public ProcessContext context() {
		return contextStack.peek();
	}

	@Override
	public Functor access( Var var ) {
		for ( ProcessContext c : contextStack ) {
			Functor f = c.assignedTo( var );
			if ( f != null ) {
				return f;
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

}
