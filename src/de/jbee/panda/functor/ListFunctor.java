package de.jbee.panda.functor;

import static de.jbee.panda.Env.just;
import de.jbee.lang.List;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Expr;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Functorizer;

final class ListFunctor
		implements Functor, ListNature, PredicateNature {

	static final Functorizer FUNCTORIZER = new ListFunctorizer();

	private final List<Functor> elems;

	ListFunctor( List<Functor> elems ) {
		super();
		this.elems = elems;
	}

	private Functor a( List<Functor> elems ) {
		return new ListFunctor( elems );
	}

	@Override
	public Functor invoke( Expr expr, EvaluationEnv env ) {
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
		//OPEN support concat
		return env.invoke( just( elems, env ), expr );
	}

	@Override
	public List<Functor> elements() {
		return elems;
	}

	@Override
	public String text() {
		return "[" + elems.length() + "]";
	}

	@Override
	public String toString() {
		return elems.toString();
	}

	static class ListFunctorizer
			implements Functorizer {

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value instanceof List<?> ) {
				return functorize( (List<?>) value, env );
			}
			if ( value instanceof String[] ) {
				return functorize( List.with.elements( (String[]) value ), env );
			}
			return env.behaviour( MAYBE, value );
		}

		private Functor functorize( List<?> list, FunctorizeEnv f ) {
			List<Functor> elems = List.with.noElements();
			for ( Object e : List.iterate.backwards( list ) ) {
				elems = elems.prepand( f.value( e ) );
			}
			return new ListFunctor( elems );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( LIST, this );
			env.install( String[].class, this );
		}
	}

	@Override
	public boolean is() {
		return !elems.isEmpty();
	}
}
