package de.jbee.panda;

import de.jbee.lang.List;

public class ListFunctor
		implements Functor, ListNature {

	private final List<Functor> elems;

	ListFunctor( List<Functor> elems ) {
		super();
		this.elems = elems;
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		if ( sel.isNone() ) {
			return this;
		}
		// regelt die listen zugriffsfunktionen: [x] usw.
		if ( sel.startsWith( "[" ) ) {
			sel = sel.skip( 1 );
			if ( sel.startsWith( "." ) ) {
				sel.skip( '.' );

			}
		}
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<Functor> elements( Environment env ) {
		return elems;
	}
}
