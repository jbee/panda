package de.jbee.panda.functor;

import static de.jbee.lang.seq.IndexFor.exists;
import static de.jbee.lang.seq.Sequences.key;
import static de.jbee.panda.Env.just;
import static de.jbee.panda.Env.nothing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.seq.Sequences;
import de.jbee.panda.BehaviouralFunctor;
import de.jbee.panda.Env;
import de.jbee.panda.EvaluationEnv;
import de.jbee.panda.Expr;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ListNature;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.Var;

public class ObjectFunctor
		implements Functor, ListNature {

	static final Functorizer FUNCTORIZER = new ReflectObjectFunctorizer();

	private final String name;
	private final Map<MemberFunctor> members;

	ObjectFunctor( Map<MemberFunctor> members ) {
		this( "", members );
	}

	ObjectFunctor( String name, Map<MemberFunctor> members ) {
		super();
		this.name = name;
		this.members = members;
	}

	@Override
	public Functor invoke( Expr expr, EvaluationEnv env ) {
		if ( expr.isEmpty() ) {
			return this;
		}
		if ( expr.after( '.' ) ) {
			String property = expr.name( null );
			if ( expr.startsWith( Expr.OBJECT ) ) {
				property = Functorizer.OBJECT;
				expr.gobble( Expr.OBJECT );
			}
			if ( property != null ) {
				return invoke( expr, env, absoluteProperty( property ) );
			}
			return env.invoke( nothing( env ), expr );
		}
		return env.invoke( just( name, env ), expr );
	}

	private Functor invoke( Expr expr, EvaluationEnv env, String property ) {
		// access of a non (sub-) object member 
		int index = members.indexFor( key( property ) );
		if ( exists( index ) ) {
			return env.invoke( members.at( index ).value().functor, expr );
		}
		// access of a (sub-) object member:
		// is there a member with the property as a prefix ?
		// find insert position - there should be a property with other prefix or not
		index = members.indexFor( Sequences.keyFirstStartsWith( property ) );
		if ( exists( index ) ) {
			MemberFunctor m = members.at( index ).value();
			if ( m.path.startsWith( property ) ) {
				return env.invoke( new ObjectFunctor( property, members ), expr );
			}
		}
		return env.invoke( nothing( env ), expr );
	}

	@Override
	public String text() {
		return "{" + name + "}";
	}

	private String absoluteProperty( String property ) {
		return name.isEmpty()
			? property
			: name + "." + property;
	}

	private static final class MemberFunctor
			implements BehaviouralFunctor {

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
		public Functor invoke( Expr expr, EvaluationEnv env ) {
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
		public String text() {
			return path;
		}

		@Override
		public boolean is() {
			return functor.is();
		}
	}

	private static final class ReflectObjectFunctorizer
			implements Functorizer {

		ReflectObjectFunctorizer() {
			// make visible
		}

		@Override
		public Functor functorize( Object value, FunctorizeEnv env ) {
			if ( value == Functor.NOTHING ) {
				return env.behaviour( MAYBE, value );
			}

			Map<MemberFunctor> members = Map.with.noEntries( Map.Entry.ORDER );
			Class<?> type = value.getClass();
			while ( type != null ) {
				for ( Field field : type.getDeclaredFields() ) {
					if ( !Modifier.isStatic( field.getModifiers() ) ) {
						field.setAccessible( true );
						try {
							String property = field.getName().replace( "_", "" );
							members = members.insert( key( property ), member( property,
									env.value( field.get( value ) ) ) );
						} catch ( Exception e ) {
							e.printStackTrace();
						}
					}
				}
				type = type.getSuperclass();
			}
			return new ObjectFunctor( members );
		}

		private MemberFunctor member( String path, Functor f ) {
			return new ObjectFunctor.MemberFunctor( path, f );
		}

		@Override
		public void setup( SetupEnv env ) {
			env.install( DEFAULT, this );
			env.install( Object.class, this );
		}

	}

	@Override
	public List<? extends Functor> elements() {
		return members.values();
	}

	@Override
	public boolean is() {
		return !members.isEmpty();
	}
}
