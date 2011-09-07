package de.jbee.panda;

public class EachFunctor
		implements Functor {

	private final Alias list;
	private final int index;

	EachFunctor( Alias list, int index ) {
		super();
		this.list = list;
		this.index = index;
	}

	@Override
	public Functor invoke( Selector sel, ExecutionEnv env ) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String value( ExecutionEnv env ) {
		return currentElement( env ).value( env );
	}

	private Functor currentElement( ExecutionEnv env ) {
		return env.invoke( env.functorFor( list ), Selector.of( "[" + index + "]" ) );
	}

	private static class EvenCase
			implements Case {

		private final Alias alias;

		EvenCase( Alias alias ) {
			super();
			this.alias = alias;
		}

		@Override
		public boolean matchesIn( ExecutionEnv env ) {
			Functor f = env.functorFor( alias );
			if ( f instanceof EachFunctor ) {

			}
			return false;
		}
	}
}
