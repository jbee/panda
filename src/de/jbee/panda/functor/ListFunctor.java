package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.ListNature;
import de.jbee.panda.Selector;

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
		if ( sel.startsWith( "[" ) ) {
			//TODO handle range
		}
		//TODO support .1..4. notation for list as part of objects

		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<Functor> elements( Environment env ) {
		return elems;
	}

	@Override
	public String text( Environment env ) {
		//OPEN list the type names of the functors ?
		return "[" + elems.length() + "]";
	}
}
