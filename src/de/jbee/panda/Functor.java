package de.jbee.panda;

public interface Functor {

	// wird immer aufgerufen bevor value() aufgerufen wird- im zweifel dann mit "" wenn keine weiteren angaben vorhanden sind
	// das kann nämlich auch falsch sein (arg erwartet) und dann wird ein Fehler-Functor geliefert

	Functor invoke( Selector sel, ExecutionEnv env );

	String value( ExecutionEnv env );
}
