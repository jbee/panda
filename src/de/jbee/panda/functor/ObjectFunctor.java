package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordering;
import de.jbee.lang.Set;
import de.jbee.panda.Accessor;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

public class ObjectFunctor
		extends ValueFunctor
		implements ListNature {

	static final TypeFunctorizer FUNCTORIZER = new ObjectFunctorizer();

	static final Ord<Object> ORDER = Order.typeaware( new MemberOrder(), MemberFunctor.class );

	private final String name;
	private final Set<MemberFunctor> members;

	ObjectFunctor( Set<MemberFunctor> members ) {
		this( "", members );
	}

	ObjectFunctor( String name, Set<MemberFunctor> members ) {
		super();
		this.name = name;
		this.members = members;
	}

	@Override
	public Functor invoke( Accessor expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '.' ) ) {
			String property = expr.property( null );
			if ( expr.startsWith( Accessor.OBJECT ) ) {
				property = TypeFunctorizer.OBJECT;
				expr.gobble( Accessor.OBJECT );
			}
			if ( property != null ) {
				return invoke( expr, env, absoluteProperty( property ) );
			}
			return env.invoke( nothing( env ), expr );
		}
		return env.invoke( just( name, env ), expr );
	}

	private Functor invoke( Accessor expr, EvaluationEnv env, String property ) {
		// access of a non (sub-) object member 
		int index = members.indexFor( new MemberFunctor( property ) );
		if ( index != ListIndex.NOT_CONTAINED ) {
			return env.invoke( members.at( index ).functor, expr );
		}
		// access of a (sub-) object member:
		// is there a member with the property as a prefix ?
		// find insert position - there should be a property with other prefix or not
		index = List.indexFor.insertBy( new MemberFunctor( property ), members.order() ).in(
				members );
		MemberFunctor m = members.at( index ); // FIXME corner case: insert would be append -> IOOBE
		if ( m.path.startsWith( property ) ) {
			return env.invoke( new ObjectFunctor( property, members ), expr );
		}
		return env.invoke( nothing( env ), expr );
	}

	@Override
	public String text( EvaluationEnv env ) {
		return "{" + name + "}";
	}

	private String absoluteProperty( String property ) {
		return name.isEmpty()
			? property
			: name + "." + property;
	}

	static final class MemberFunctor
			extends ValueFunctor {

		final String path;
		final Functor functor;

		MemberFunctor( String path ) {
			this( path, MaybeFunctor.NOTHING_INSTANCE );
		}

		MemberFunctor( String path, Functor functor ) {
			super();
			this.path = path;
			this.functor = functor;
		}

		@Override
		public String toString() {
			return path + "->" + functor.getClass().getSimpleName();
		}

		@Override
		public Functor invoke( Accessor expr, EvaluationEnv env ) {
			if ( expr.after( '.' ) ) {
				if ( expr.after( "name" ) ) {
					return env.invoke( env.functorize().value( path ), expr );
				}
				if ( expr.after( "value" ) ) {
					return env.invoke( functor, expr );
				}
				return env.invoke( nothing( env ), expr );
			}
			return env.invoke( functor, expr );
		}

		@Override
		public void processedAs( Var var, ProcessingEnv env ) {
			functor.processedAs( var, env );
		}

		@Override
		public String text( EvaluationEnv env ) {
			return path;
		}
	}

	static final class MemberOrder
			implements Ord<MemberFunctor> {

		@Override
		public Ordering ord( MemberFunctor left, MemberFunctor right ) {
			return Order.alphabetical.ord( left.path, right.path );
		}

	}

	private static final class ObjectFunctorizer
			implements TypeFunctorizer {

		ObjectFunctorizer() {
			// make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value == NOTHING ) {
				return f.behaviour( MAYBE, value );
			}
			// TODO real object functorization
			Set<MemberFunctor> elements = Set.with.elements( ORDER, List.with.elements( member(
					OBJECT + ".type", value.getClass().getCanonicalName(), f ), member( OBJECT
					+ ".text", String.valueOf( value ), f ) ) );
			return new ObjectFunctor( elements );
		}

		private MemberFunctor member( String path, String text, Functorizer f ) {
			return new ObjectFunctor.MemberFunctor( path, f.value( text ) );
		}

		@Override
		public void install( SetupEnv env ) {
			env.install( DEFAULT, this );
			env.install( Object.class, this );
		}

	}

	@Override
	public List<? extends Functor> elements( EvaluationEnv env ) {
		return members;
	}
}
