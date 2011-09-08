package de.jbee.panda;

public class BooleanFunctor
		implements Functor, BooleanNature, IntegerNature, TextNature {

	private final boolean value;

	BooleanFunctor( boolean value ) {
		super();
		this.value = value;
	}

	@Override
	public Functor invoke( Selector sel, Environment env ) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String text( Environment env ) {
		return String.valueOf( value );
	}

	@Override
	public boolean bool( Environment env ) {
		return value;
	}

	@Override
	public int integer( Environment ent ) {
		return value
			? 1
			: 0;
	}

}
