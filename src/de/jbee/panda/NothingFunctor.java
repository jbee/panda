package de.jbee.panda;

import de.jbee.lang.List;

final class NothingFunctor
		implements Functor, BooleanNature, IntegerNature, TextNature, ListNature {

	public static final Functor INSTANCE = new NothingFunctor();

	private NothingFunctor() {
		// hide
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean bool( Environment env ) {
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
