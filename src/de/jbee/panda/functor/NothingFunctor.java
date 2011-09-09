package de.jbee.panda.functor;

import de.jbee.lang.List;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.IntegralNature;
import de.jbee.panda.ListNature;
import de.jbee.panda.PredicateNature;
import de.jbee.panda.Selector;

public final class NothingFunctor
		implements Functor, PredicateNature, IntegralNature, ListNature {

	public static final Functor INSTANCE = new NothingFunctor();

	private NothingFunctor() {
		// hide
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		// TODO tell env about invocation on nothing
		return this;
	}

	@Override
	public boolean is( Environment env ) {
		return false;
	}

	@Override
	public int integer( Environment ent ) {
		return 0;
	}

	@Override
	public String text( Environment env ) {
		return "";
	}

	@Override
	public List<Functor> elements( Environment env ) {
		return List.with.noElements();
	}

}
