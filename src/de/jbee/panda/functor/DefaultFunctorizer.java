package de.jbee.panda.functor;

import java.util.HashMap;
import java.util.Map;

import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.SetupEnv;
import de.jbee.panda.TypeFunctorizer;

public class DefaultFunctorizer
		implements SetupEnv, Functorizer {

	private static DefaultFunctorizer instance = new DefaultFunctorizer();

	static {
		BooleanFunctor.FUNCTORIZER.install( instance );
		IntegerFunctor.FUNCTORIZER.install( instance );
		StringFunctor.FUNCTORIZER.install( instance );
		ListFunctor.FUNCTORIZER.install( instance );
		MaybeFunctor.FUNCTORIZER.install( instance );
		EachFunctor.FUNCTORIZER.install( instance );
		ObjectFunctor.FUNCTORIZER.install( instance );
	}

	public static Functorizer getInstance() {
		return instance;
	}

	public final Map<String, TypeFunctorizer> types = new HashMap<String, TypeFunctorizer>();

	@Override
	public void install( String name, TypeFunctorizer functorizer ) {
		types.put( name, functorizer );
	}

	@Override
	public void install( Class<?> type, TypeFunctorizer functorizer ) {
		install( name( type ), functorizer );
	}

	@Override
	public Functor value( Object value ) {
		if ( value instanceof Functor ) {
			return (Functor) value;
		}
		return get( name( value ), get( TypeFunctorizer.DEFAULT, MaybeFunctor.FUNCTORIZER ) ).functorize(
				value, this );
	}

	private String name( Object value ) {
		return name( value == null
			? Void.class
			: value.getClass() );
	}

	public String name( Class<?> type ) {
		return type.getCanonicalName();
	}

	@Override
	public Functor behaviour( String name, Object value ) {
		return get( name, MaybeFunctor.FUNCTORIZER ).functorize( value, this );
	}

	private TypeFunctorizer get( String name, TypeFunctorizer fallback ) {
		TypeFunctorizer tf = types.get( name );
		return tf == null
			? fallback
			: tf;
	}

	@Override
	public void install( String name, Functor constant ) {
		install( name, new ConstantFunctorizer( constant ) );
	}
}
