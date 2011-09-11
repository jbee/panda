package de.jbee.panda.functor;

import de.jbee.lang.Equal;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordering;
import de.jbee.lang.Set;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Selector;

public class ObjectFunctor
		extends ValueFunctor {

	private final String name;
	private final Set<Member> members;

	ObjectFunctor( String name, Set<Member> members ) {
		super();
		this.name = name;
		this.members = members;
	}

	@Override
	public Functor invoke( Selector arg, Environment env ) {
		if ( arg.isNone() ) {
			return this;
		}
		String property = arg.readPattern( "\\w+(\\.\\w+)*" );
		//TODO replace with binary search index lookup
		int index = Set.indexFor.elemBy( new Member( property( property ) ),
				Equal.by( members.order() ) ).in( members );
		if ( index >= 0 ) {
			return members.at( index ).functor;
		}
		// TODO does entries with the property prefix exists ?
		// find insert position - there should be a property with other suffix or not
		index = List.indexFor.insertBy( new Member( property ), members.order() ).in( members );
		Member m = members.at( index ); // FIXME the correct index has to be computed
		if ( m.path.startsWith( property ) ) {
			return new ObjectFunctor( property, members );
		}
		return env.invoke( Functor.NOTHING, arg );
	}

	@Override
	public String text( Environment env ) {
		return "{" + name + "}";
	}

	private String property( String property ) {
		return name.isEmpty()
			? property
			: name + "." + property;
	}

	static class Member {

		final String path;
		final Functor functor;

		Member( String path ) {
			this( path, Functor.NOTHING );
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

}
