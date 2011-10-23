package de.jbee.panda.functor;

import static de.jbee.panda.Env.nothing;
import static de.jbee.panda.Expr.elemAt;
import static java.lang.Integer.MAX_VALUE;
import de.jbee.lang.List;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.ProcessContext;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Expr;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Var;

/**
 * It provides a iteration functionality for lists and object-properties and behaves transparent for
 * all other values.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class EachFunctor
		implements BehaviouralFunctor {

	static final Functorizer FUNCTORIZER = new EachFunctorizer();

	private final Functor elements;
	private final int index;

	EachFunctor( Functor elements ) {
		this( elements, 0 );
	}

	private EachFunctor( Functor elements, int index ) {
		super();
		if ( ! ( elements instanceof ListNature ) ) {
			System.out.println( "warning " + elements );
		}
		this.elements = elements;
		this.index = index;
	}

	private Functor a( Functor list, int index ) {
		return new EachFunctor( list, index );
	}

	@Override
	public Functor invoke( Expr expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return env.invoke( currentElement( env ), expr );
		}
		if ( expr.after( '#' ) ) {
			return env.invoke( env.functorize().value( index ), expr );
		}
		if ( expr.after( '[' ) ) {
			int start = expr.index( 0 );
			if ( expr.after( ':' ) ) {
				Functor sl = elements.invoke( Expr.range( start, expr.index( MAX_VALUE ) ), env );
				return env.invoke( a( sl, index ), expr.gobble( ']' ) );
			}
			return env.invoke( elements, Expr.elemAt( start ).join( expr.gobble( ']' ) ) );
		}
		return env.invoke( currentElement( env ), expr );
	}

	@Override
	public String text() {
		return ( elements instanceof ListNature )
			? ( (ListNature) elements ).elements().at( index ).text()
			: elements.text();
	}

	private Functor currentElement( EvaluationEnv env ) {
		return env.invoke( elements, elemAt( index ) );
	}

	@Override
	public String toString() {
		return "each[" + index + "] " + elements;
	}

	@Override
	public void bind( Var var, ProcessingEnv env ) {
		if ( elements instanceof ListNature ) {
			ListNature l = (ListNature) elements;
			List<? extends Functor> elems = l.elements();
			if ( !elems.isEmpty() ) {
				env.context().addDependency( var );
			}
		}
	}

	@Override
	public void rebind( Var var, ProcessingEnv env ) {
		if ( elements instanceof ListNature ) {
			ListNature l = (ListNature) elements;
			List<? extends Functor> elems = l.elements();
			int idx = index + 1;
			ProcessContext context = env.context();
			if ( idx < elems.length() ) {
				context.define( var, new EachFunctor( elements, idx ) );
			} else {
				context.define( var, nothing( env ) );
			}
		}
	}

	static final class EachFunctorizer
			implements Functorizer {

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			return new EachFunctor( env.value( value ) );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( "each", this );
		}

	}

	@Override
	public boolean is() {
		return elements.is();
	}
}
