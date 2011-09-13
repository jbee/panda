package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Accessor;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.TypeFunctorizer;

final class ListFunctor
		extends ValueFunctor
		implements ListNature {

	static final TypeFunctorizer FUNCTORIZER = new ListFunctorizer();

	private final List<Functor> elems;

	ListFunctor( List<Functor> elems ) {
		super();
		this.elems = elems;
	}

	private Functor a( List<Functor> elems ) {
		return new ListFunctor( elems );
	}

	@Override
	public Functor invoke( Accessor expr, Environment env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '[' ) ) {
			int start = expr.index( 0 );
			if ( expr.after( ':' ) ) {
				int end = expr.index( elems.length() - 1 );
				List<Functor> sublist = List.which.takesFromTo( start, end ).from( elems );
				return env.invoke( a( sublist ), expr.gobble( ']' ) );
			}
			return env.invoke( elems.at( start ), expr.gobble( ']' ) );
		}
		//OPEN support .1..4. notation for list as part of objects
		return env.invoke( JUST, expr );
	}

	@Override
	public List<Functor> elements( Environment env ) {
		return elems;
	}

	@Override
	public String text( Environment env ) {
		return "[" + elems.length() + "]";
	}

	static class ListFunctorizer
			implements TypeFunctorizer {

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof List<?> ) {
				List<?> l = (List<?>) value;
				List<Functor> elems = List.with.noElements();
				for ( Object e : List.iterate.backwards( l ) ) {
					elems = elems.prepand( f.value( e ) );
				}
				return new ListFunctor( elems );
			}
			return NOTHING;
		}
	}
}
