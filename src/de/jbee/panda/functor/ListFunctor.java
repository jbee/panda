package de.jbee.panda.functor;

import static de.jbee.panda.Env.just;
import de.jbee.lang.List;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

final class ListFunctor
		implements Functor, ListNature, PredicateNature {

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
	public Functor invoke( Selector expr, EvaluationEnv env ) {
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
	public List<Functor> elements( EvaluationEnv env ) {
		return elems;
	}

	@Override
	public String text( EvaluationEnv env ) {
		return "[" + elems.length() + "]";
	}

	static class ListFunctorizer
			implements TypeFunctorizer {

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value instanceof List<?> ) {
				return functorize( (List<?>) value, f );
			}
			if ( value instanceof String[] ) {
				return functorize( List.with.elements( (String[]) value ), f );
			}
			return f.behaviour( MAYBE, value );
		}

		private Functor functorize( List<?> list, Functorizer f ) {
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
	public boolean is( EvaluationEnv env ) {
		return !elems.isEmpty();
	}
}
