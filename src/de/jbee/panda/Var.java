package de.jbee.panda;

import java.io.Serializable;

public final class Var
		implements Serializable {

	private final String name;

	public static Var named( String name ) {
		if ( name == null || name.isEmpty() ) {
			throw new IllegalArgumentException( "A variable has to have a name." );
		}
		return new Var( name );
	}

	private Var( String name ) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof Var ) {
			return ( (Var) obj ).name.equals( name );
		}
		return false;
	}

}
