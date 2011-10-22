package de.jbee.panda;

import java.io.Serializable;

public final class Var
		implements Serializable {

	public static final Var UNDEFINED = new Var( "" );

	private final String name;

	public static Var named( String name ) {
		if ( name == null || name.isEmpty() ) {
			return UNDEFINED;
		}
		return new Var( name );
	}

	private Var( String name ) {
		super();
		this.name = name;
	}

	public boolean isUndefined() {
		return name.equals( UNDEFINED.name );
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public boolean equalTo( Var other ) {
		return name.equals( other.name );
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof Var ) {
			return equalTo( (Var) obj );
		}
		return false;
	}

}
