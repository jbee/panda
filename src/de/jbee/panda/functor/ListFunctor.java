package de.jbee.panda.functor;

import static de.jbee.panda.functor.Functoring.a;
import de.jbee.lang.List;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.ListNature;
import de.jbee.panda.Selector;

final class ListFunctor
		extends ValueFunctor
		implements ListNature {

	private final List<Functor> elems;

	ListFunctor( List<Functor> elems ) {
		super();
		this.elems = elems;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.isNone() ) {
			return this;
		}
		if ( arg.after( '[' ) ) {
			int start = arg.integer( 0 );
			if ( arg.after( ':' ) ) {
				List<Functor> sublist = List.which.takesFromTo( start,
						arg.integer( elems.length() - 1 ) ).from( elems );
				return env.invoke( a( sublist ), arg.skip( ']' ) );
			}
			return env.invoke( elems.at( start ), arg.skip( ']' ) );
		}
		//TODO support .1..4. notation for list as part of objects

		return env.invoke( NOTHING, arg );
	}

	@Override
	public List<Functor> elements( Environment env ) {
		return elems;
	}

	@Override
	public String text( Environment env ) {
		return "[" + elems.length() + "]";
	}
}
