/**
 * 
 */
package de.jbee.panda;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Context
		implements ProcessContext {

	private final Map<Var, Functor> vars = new HashMap<Var, Functor>();
	private final Map<Var, Functor> used = new HashMap<Var, Functor>();
	private final Set<Var> dependencies = new HashSet<Var>();
	private boolean bound;

	@Override
	public void define( Var var, Functor f ) {
		vars.put( var, f );
	}

	@Override
	public void addDependency( Var var ) {
		dependencies.add( var );
	}

	@Override
	public Functor definedAs( Var var, Functor undefined ) {
		Functor res = vars.get( var );
		if ( res != null ) {
			used.put( var, res );
			return res;
		}
		return undefined;
	}

	@Override
	public boolean processed( ProcessingEnv env ) {
		if ( !bound ) {
			bind( env );
			bound = true;
			return dependencies.isEmpty()
				? false
				: noDependencies( env );
		}
		rebind( env );
		return noDependencies( env );
	}

	private boolean noDependencies( ProcessingEnv env ) {
		boolean res = true;
		for ( Var d : dependencies ) {
			Functor f = definedAs( d, null );
			if ( f != null && f.is( env ) ) {
				return false;
			}
		}
		return res;
	}

	private void rebind( ProcessingEnv env ) {
		for ( Entry<Var, Functor> e : used.entrySet() ) {
			Env.rebind( e.getKey(), e.getValue(), env );
		}
	}

	private void bind( ProcessingEnv env ) {
		for ( Entry<Var, Functor> e : vars.entrySet() ) {
			Env.bind( e.getKey(), e.getValue(), env );
		}
	}

}