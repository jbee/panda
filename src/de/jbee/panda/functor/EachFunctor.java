package de.jbee.panda.functor;

import static de.jbee.panda.Accessor.elemAt;
import static java.lang.Integer.MAX_VALUE;
import de.jbee.lang.List;
import de.jbee.panda.Accessor;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.ListNature;
import de.jbee.panda.Var;

public class EachFunctor
		implements Functor {

	private final Functor list;
	private final int index;

	EachFunctor( Functor list ) {
		this( list, 0 );
	}

	private EachFunctor( Functor list, int index ) {
		super();
		this.list = list;
		this.index = index;
	}

	private Functor a( Functor list, int index ) {
		return new EachFunctor( list, index );
	}

	@Override
	public Functor invoke( Accessor expr, Environment env ) {
		if ( expr.isEmpty() ) {
			return env.invoke( currentElement( env ), expr );
		}
		if ( expr.after( '#' ) ) {
			return env.invoke( env.functorize().value( index ), expr );
		}
		if ( expr.after( '[' ) ) {
			int start = expr.index( 0 );
			if ( expr.after( ':' ) ) {
				Functor sl = list.invoke( Accessor.range( start, expr.index( MAX_VALUE ) ), env );
				return env.invoke( a( sl, index ), expr.gobble( ']' ) );
			}
			return env.invoke( list, Accessor.elemAt( start ).join( expr.gobble( ']' ) ) );
		}
		return env.invoke( currentElement( env ), expr );
	}

	@Override
	public String text( Environment env ) {
		return currentElement( env ).text( env );
	}

	private Functor currentElement( Environment env ) {
		return env.invoke( list, elemAt( index ) );
	}

	@Override
	public void renderedAs( Var var, Environment env ) {
		if ( list instanceof ListNature ) {
			ListNature l = (ListNature) list;
			List<Functor> elems = l.elements( env );
			int idx = index + 1;
			if ( idx < elems.length() ) {
				env.let( var, new EachFunctor( list, idx ) );
				env.renderFrom( 0 ); // zero of the current block
			}
		}
	}

}
