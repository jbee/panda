package de.jbee.panda.functor;

import java.util.HashMap;
import java.util.Map;

import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.SetupEnv;

public class DefaultFunctorizer
		implements SetupEnv, FunctorizeEnv {

	private static DefaultFunctorizer instance = new DefaultFunctorizer();

	static {
		BooleanFunctor.FUNCTORIZER.setup( instance );
		IntegerFunctor.FUNCTORIZER.setup( instance );
		StringFunctor.FUNCTORIZER.setup( instance );
		ListFunctor.FUNCTORIZER.setup( instance );
		MaybeFunctor.FUNCTORIZER.setup( instance );
		EachFunctor.FUNCTORIZER.setup( instance );
		ObjectFunctor.FUNCTORIZER.setup( instance );
		CaseFunctor.FUNCTORIZER.setup( instance );
		DefineFunctor.FUNCTORIZER.setup( instance );
		VarFunctor.FUNCTORIZER.setup( instance );
	}

	public static FunctorizeEnv getInstance() {
		return instance;
	}

	public final Map<String, Functorizer> types = new HashMap<String, Functorizer>();

	@Override
	public Functor behaviour( String name, Object value ) {
		return get( name, MaybeFunctor.FUNCTORIZER ).functorize( value, this );
	}

	@Override
	public void install( Class<?> type, Functorizer functorizer ) {
		install( name( type ), functorizer );
	}

	@Override
	public void install( String name, Functor constant ) {
		install( name, new ConstantFunctorizer( name, constant ) );
	}

	@Override
	public void install( String name, Functorizer functorizer ) {
		types.put( name, functorizer );
	}

	@Override
	public Functor value( Object value ) {
		if ( value instanceof Functor ) {
			return (Functor) value;
		}
		return get( name( value ), get( Functorizer.DEFAULT, MaybeFunctor.FUNCTORIZER ) ).functorize(
				value, this );
	}

	private Functorizer get( String name, Functorizer fallback ) {
		Functorizer tf = types.get( name );
		return tf == null
			? fallback
			: tf;
	}

	private String name( Class<?> type ) {
		return type.getCanonicalName();
	}

	private String name( Object value ) {
		return name( value == null
			? Void.class
			: value.getClass() );
	}
}
