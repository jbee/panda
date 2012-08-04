package de.jbee.panda;

import static de.jbee.panda.Env.nothing;
import de.jbee.lang.List;
import de.jbee.panda.functor.Functorize;

public class Environment
		implements ProcessingEnv {

	private List<ProcessContext> contextStack = List.with.noElements();

	public Environment( ProcessContext context ) {
		contextStack = contextStack.prepand( context ); // the master context
	}

	@Override
	public ProcessContext context() {
		return contextStack.at( 0 );
	}

	@Override
	public Functor value( Var var ) {
		if ( !var.isUndefined() ) {
			for ( ProcessContext c : List.iterate.forwards( contextStack ) ) {
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
		contextStack = contextStack.prepand( context );
	}

	@Override
	public void close( ProcessContext context ) {
		while ( contextStack.length() > 1 && contextStack.at( 0 ) != context ) {
			contextStack = contextStack.drop( 1 );
		}
		if ( contextStack.length() > 1 && contextStack.at( 0 ) == context ) {
			contextStack = contextStack.drop( 1 );
		}
	}

	@Override
	public void define( Var var, Functor f ) {
		context().define( var, f );
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
		//OPEN check here for binary ops ?
		return invoke( functorize().behaviour( name, eval( expr ) ), selector );
	}

}
