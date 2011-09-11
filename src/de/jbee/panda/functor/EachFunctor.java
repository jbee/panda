package de.jbee.panda.functor;

import static de.jbee.panda.Selector.elemAt;
import static de.jbee.panda.functor.Functoring.a;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;
import de.jbee.panda.Var;

public class EachFunctor
		implements Functor {

	private final Functor list;
	private final int index;

	EachFunctor( Functor list, int index ) {
		super();
		this.list = list;
		this.index = index;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.isNone() ) {
			return env.invoke( currentElement( env ), arg );
		}
		if ( arg.after( '#' ) ) {
			return env.invoke( a( index ), arg );
		}
		if ( arg.after( '[' ) ) {
			int start = arg.integer( 0 );
			if ( arg.after( ':' ) ) {
				//OPEN Integer.MAX_VALUE ersetzen durch die abfrage des max-value vom list functor ?
				Functor sublist = list.invoke( Selector.range( start,
						arg.integer( Integer.MAX_VALUE ) ), env );
				return env.invoke( new EachFunctor( sublist, index ), arg.skip( ']' ) );
			}
			return env.invoke( list, Selector.elemAt( start ).join( arg.skip( ']' ) ) );
		}
		return env.invoke( currentElement( env ), arg );
	}

	@Override
	public String text( Environment env ) {
		return currentElement( env ).text( env );
	}

	private Functor currentElement( Environment env ) {
		return env.invoke( list, elemAt( index ) );
	}

	@Override
	public void bind( Var var, Environment env ) {
		// TODO Auto-generated method stub

	}

}
