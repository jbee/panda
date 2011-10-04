package de.jbee.panda.functor;

import static de.jbee.panda.Accessor.elemAt;
import static java.lang.Integer.MAX_VALUE;
import de.jbee.lang.List;
import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.ProcessContext;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

/**
 * It provides a iteration functionality for lists and object-properties and behaves transparent for
 * all other values.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class EachFunctor
		implements Functor {

	static final TypeFunctorizer FUNCTORIZER = new EachFunctorizer();

	private final Functor elements;
	private final int index;

	EachFunctor( Functor list ) {
		this( list, 0 );
	}

	private EachFunctor( Functor list, int index ) {
		super();
		this.elements = list;
		this.index = index;
	}

	private Functor a( Functor list, int index ) {
		return new EachFunctor( list, index );
	}

	@Override
	public Functor invoke( Accessor expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return env.invoke( currentElement( env ), expr );
		}
		if ( expr.after( '#' ) ) {
			return env.invoke( env.functorize().value( index ), expr );
		}
		if ( expr.after( '[' ) ) {
			int start = expr.index( 0 );
			if ( expr.after( ':' ) ) {
				Functor sl = elements.invoke( Accessor.range( start, expr.index( MAX_VALUE ) ), env );
				return env.invoke( a( sl, index ), expr.gobble( ']' ) );
			}
			return env.invoke( elements, Accessor.elemAt( start ).join( expr.gobble( ']' ) ) );
		}
		return env.invoke( currentElement( env ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return currentElement( env ).text( env );
	}

	private Functor currentElement( EvaluationEnv env ) {
		return env.invoke( elements, elemAt( index ) );
	}

	@Override
	public void processedAs( Var var, ProcessingEnv env ) {
		if ( elements instanceof ListNature ) {
			ListNature l = (ListNature) elements;
			List<? extends Functor> elems = l.elements( env );
			int idx = index + 1;
			if ( idx < elems.length() ) {
				ProcessContext context = env.context();
				context.let( var, new EachFunctor( elements, idx ) );
				context.renderFrom( 0 ); // start of the current block
			}
		}
	}

	static final class EachFunctorizer
			implements TypeFunctorizer {

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			return new EachFunctor( f.value( value ) );
		}

		@Override
		public void install( SetupEnv env ) {
			env.install( "each", this );
		}

	}

}
