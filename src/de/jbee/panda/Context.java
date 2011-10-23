/**
 * 
 */
package de.jbee.panda;

import java.util.HashSet;
import java.util.Set;

import de.jbee.lang.List;
import de.jbee.lang.Traversal;

public class Context
		implements ProcessContext {

	private List<Definition> defs = List.with.noElements();

	private final Set<Var> dependencies = new HashSet<Var>();
	private boolean bound;

	@Override
	public void addDependency( Var var ) {
		dependencies.add( var );
	}

	@Override
	public void define( Var var, Functor f ) {
		// we don't replace - instead we prepand and take first during lookup 
		// thereby we get a history and allow calling define during bind or rebind 
		defs = defs.prepand( new Definition( var, f ) );
		override( var );
	}

	@Override
	public Functor definedAs( Var var, Functor undefined ) {
		return definedAs( defs, var, undefined );
	}

	@Override
	public boolean processed( ProcessingEnv env ) {
		if ( !bound ) {
			bind( env, 0 );
			bound = true;
			return dependencies.isEmpty()
				? false
				: !hasOperativeDependencies();
		}
		bind( env, defs.length() );
		return !hasOperativeDependencies();
	}

	private void bind( ProcessingEnv env, int rebinds ) {
		int bound = 0;
		int pos = defs.length() - 1;
		while ( pos >= 0 ) {
			Definition def = defs.at( pos );
			if ( !def.overridden ) {
				if ( bound < rebinds ) {
					Env.rebind( def.name, def.value, env );
				} else {
					Env.bind( def.name, def.value, env );
				}
			}
			bound++;
			pos = defs.length() - 1 - bound;
		}
	}

	private Functor definedAs( List<Definition> vars, Var var, Functor undefined ) {
		for ( int i = 0; i < vars.length(); i++ ) {
			Definition def = vars.at( i );
			if ( def.name.equalTo( var ) ) {
				return def.value;
			}
		}
		return undefined;
	}

	private boolean hasOperativeDependencies() {
		boolean res = false;
		for ( Var d : dependencies ) {
			Functor f = definedAs( d, null );
			if ( f != null && f.is() ) {
				return true;
			}
		}
		return res;
	}

	private void override( final Var var ) {
		defs.traverse( 1, new Traversal<Definition>() {

			@Override
			public int incrementOn( Definition e ) {
				if ( e.name.equalTo( var ) ) {
					e.overridden = true;
					return STOP_TRAVERSAL;
				}
				return 1;
			}
		} );
	}

	private static final class Definition {

		final Var name;
		final Functor value;

		boolean overridden = false;

		Definition( Var name, Functor value ) {
			super();
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			final String res = name + " => " + value;
			return overridden
				? "(" + res + ")"
				: res;
		}

	}

}