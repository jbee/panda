package de.jbee.panda.functor;

import static de.jbee.panda.Env.just;
import static de.jbee.panda.Env.nothing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordering;
import de.jbee.lang.Set;
import de.jbee.panda.BehavioralFunctor;
import de.jbee.panda.Env;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Selector;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;
import de.jbee.panda.Var;

public class ObjectFunctor
		implements Functor, ListNature {

	static final TypeFunctorizer FUNCTORIZER = new ReflectObjectFunctorizer();

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
	public Functor invoke( Selector expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '.' ) ) {
			String property = expr.property( null );
			if ( expr.startsWith( Selector.OBJECT ) ) {
				property = TypeFunctorizer.OBJECT;
				expr.gobble( Selector.OBJECT );
			}
			if ( property != null ) {
				return invoke( expr, env, absoluteProperty( property ) );
			}
			return env.invoke( nothing( env ), expr );
		}
		return env.invoke( just( name, env ), expr );
	}

	private Functor invoke( Selector expr, EvaluationEnv env, String property ) {
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
			implements BehavioralFunctor {

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
		public Functor invoke( Selector expr, EvaluationEnv env ) {
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
		public void bind( Var var, ProcessingEnv env ) {
			Env.bind( var, functor, env );
		}

		@Override
		public void rebind( Var var, ProcessingEnv env ) {
			Env.rebind( var, functor, env );
		}

		@Override
		public String text( EvaluationEnv env ) {
			return path;
		}

		@Override
		public boolean is( EvaluationEnv env ) {
			return functor.is( env );
		}
	}

	static final class MemberOrder
			implements Ord<MemberFunctor> {

		@Override
		public Ordering ord( MemberFunctor left, MemberFunctor right ) {
			return Order.alphabetical.ord( left.path, right.path );
		}

	}

	private static final class ReflectObjectFunctorizer
			implements TypeFunctorizer {

		ReflectObjectFunctorizer() {
			// make visible
		}

		@Override
		public Functor functorize( Object value, Functorizer f ) {
			if ( value == NOTHING ) {
				return f.behaviour( MAYBE, value );
			}

			List<MemberFunctor> elements = List.with.noElements();
			Class<?> type = value.getClass();
			while ( type != null ) {
				for ( Field field : type.getDeclaredFields() ) {
					if ( !Modifier.isStatic( field.getModifiers() ) ) {
						field.setAccessible( true );
						try {
							elements = elements.prepand( member(
									field.getName().replace( "_", "" ),
									f.value( field.get( value ) ) ) );
						} catch ( Exception e ) {
							e.printStackTrace();
						}
					}
				}
				type = type.getSuperclass();
			}
			return new ObjectFunctor( Set.with.elements( ORDER, elements ) );
		}

		private MemberFunctor member( String path, Functor f ) {
			return new ObjectFunctor.MemberFunctor( path, f );
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

	@Override
	public boolean is( EvaluationEnv env ) {
		return !members.isEmpty();
	}
}
