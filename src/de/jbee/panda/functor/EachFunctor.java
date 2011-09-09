package de.jbee.panda.functor;

import static de.jbee.panda.Selector.elemAt;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;
import de.jbee.panda.TextNature;

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
	public Functor invoke( Selector sel, Environment env ) {
		if ( sel.isNone() ) {
			return currentElement( env );
		}
		if ( sel.startsWith( "#" ) ) {
			return env.invoke( new IntegerFunctor( index ), sel.skip( 1 ) );
		}
		if ( sel.startsWith( "[" ) ) {
			// if its a range build a sub-each functor

			// if it is a single index delegate to the ListFunctor refered by this
		}
		//TODO warning about unknown selector
		return this;
	}

	@Override
	public String text( Environment env ) {
		Functor e = currentElement( env );
		return e instanceof TextNature
			? ( (TextNature) e ).text( env )
			: "";
	}

	private Functor currentElement( Environment env ) {
		return env.invoke( list, elemAt( index ) );
	}

}
