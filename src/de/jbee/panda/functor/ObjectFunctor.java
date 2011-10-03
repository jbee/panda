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
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

public class ObjectFunctor
		extends ValueFunctor {

	static final TypeFunctorizer FUNCTORIZER = new ObjectFunctorizer();

	static final Ord<Object> ORDER = Order.typeaware( new MemberOrd(), Member.class );

	private final String name;
	private final Set<Member> members;

	ObjectFunctor( Set<Member> members ) {
		this( "", members );
	}

	ObjectFunctor( String name, Set<Member> members ) {
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
			if ( expr.startsWith( '0' ) ) {
				property = TypeFunctorizer.OBJECT;
				expr.gobble( '0' );
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
		int index = members.indexFor( new Member( property ) );
		if ( index != ListIndex.NOT_CONTAINED ) {
			return env.invoke( members.at( index ).functor, expr );
		}
		// access of a (sub-) object member:
		// is there a member with the property as a prefix ?
		// find insert position - there should be a property with other prefix or not
		index = List.indexFor.insertBy( new Member( property ), members.order() ).in( members );
		Member m = members.at( index ); // FIXME corner case: insert would be append -> IOOBE
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

	static class Member {

		final String path;
		final Functor functor;

		Member( String path ) {
			this( path, MaybeFunctor.NOTHING_INSTANCE );
		}

		Member( String path, Functor functor ) {
			super();
			this.path = path;
			this.functor = functor;
		}

		@Override
		public String toString() {
			return path + "->" + functor.getClass().getSimpleName();
		}
	}

	static final class MemberOrd
			implements Ord<Member> {

		@Override
		public Ordering ord( Member left, Member right ) {
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
			if ( value == null ) {
				return f.behaviour( MAYBE, value );
			}
			// TODO Auto-generated method stub
			if ( true ) {
				return new ObjectFunctor( Set.with.elements( ORDER, List.with.elements( member(
						OBJECT + ".type", value.getClass().getCanonicalName(), f ), member( OBJECT
						+ ".text", String.valueOf( value ), f ) ) ) );
			}
			return f.behaviour( MAYBE, value );
		}

		private Member member( String path, String text, Functorizer f ) {
			return new ObjectFunctor.Member( path, f.value( text ) );
		}

		@Override
		public void install( SetupEnv env ) {
			env.install( DEFAULT, this );
			env.install( Object.class, this );
		}

	}
}
