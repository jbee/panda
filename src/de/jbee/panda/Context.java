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

	@Override
	public void bind( Var var, Functor f ) {
		vars.put( var, f );
	}

	@Override
	public void addDependency( Var var ) {
		dependencies.add( var );
	}

	@Override
	public Functor boundTo( Var var, Functor unbound ) {
		Functor res = vars.get( var );
		if ( res != null ) {
			used.put( var, res );
			return res;
		}
		return unbound;
	}

	@Override
	public boolean independent( ProcessingEnv env ) {
		boolean res = true;
		for ( Var d : dependencies ) {
			Functor f = boundTo( d, null );
			if ( f != null && f instanceof PredicateNature && ! ( (PredicateNature) f ).is( env ) ) {
				return false;
			}
		}
		return res;
	}

	@Override
	public void rebind( ProcessingEnv env ) {
		for ( Entry<Var, Functor> e : used.entrySet() ) {
			Env.rebind( e.getKey(), e.getValue(), env );
		}
	}

	@Override
	public void bind( ProcessingEnv env ) {
		for ( Entry<Var, Functor> e : vars.entrySet() ) {
			Env.bind( e.getKey(), e.getValue(), env );
		}
	}

}