package de.jbee.panda;

public class EachFunctor
		implements Functor {

	private int index;

	@Override
	public Functor invoke( Instruction arg, ExecutionEnv env ) {
		if ( arg.isNone() ) {
			//TODO return functor of current index
		}
		if ( arg.startsWith( "#" ) ) {
			return env.invoke( new IntegerFunctor( index ), arg.skip( 1 ) );
		}
		if ( arg.startsWith( "[" ) ) {
			// if its a range build a sub-each functor

			// if it is a single index delegate to the ListFunctor refered by this
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String value( ExecutionEnv nev ) {
		// TODO Auto-generated method stub
		return null;
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
