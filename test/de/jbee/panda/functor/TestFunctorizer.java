package de.jbee.panda.functor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.panda.Selector;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.TypeFunctorizer;

public class TestFunctorizer {

	static class ObjectType {

	}

	@Test
	public void testYieldIntegerFunctor() {
		Functorizer f = DefaultFunctorizer.getInstance();
		assertThat( f.value( 1 ), is( IntegerFunctor.class ) );
		assertThat( f.value( 1L ), is( IntegerFunctor.class ) );
		assertThat( f.behaviour( TypeFunctorizer.NUMBER, 2.3 ), is( IntegerFunctor.class ) );
	}

	@Test
	public void testYieldStringFunctor() {
		Functorizer f = DefaultFunctorizer.getInstance();
		assertThat( f.value( "test" ), is( StringFunctor.class ) );
		assertThat( f.value( "test".toCharArray() ), is( StringFunctor.class ) );
		assertThat( f.behaviour( TypeFunctorizer.TEXT, "abc" ), is( StringFunctor.class ) );
	}

	@Test
	public void testYieldListFunctor() {
		Functorizer f = DefaultFunctorizer.getInstance();
		assertThat( f.behaviour( TypeFunctorizer.LIST, List.with.noElements() ),
				is( ListFunctor.class ) );
		assertThat( f.behaviour( TypeFunctorizer.LIST, List.with.elements( 1, 2 ) ),
				is( ListFunctor.class ) );
	}

	@Test
	public void testYieldObjectFunctor() {
		ProcessingEnv env = new Environment();
		Functor obj = env.functorize().value( new ObjectType() );
		assertThat( obj, is( ObjectFunctor.class ) );
		Functor e = obj.invoke( Selector.of( "." + Selector.OBJECT ), env );
		assertThat( e, is( ObjectFunctor.class ) );
		assertThat( e.invoke( Selector.of( ".type" ), env ), is( StringFunctor.class ) );
		e = obj.invoke( Selector.of( "." + Selector.OBJECT + ".type" ), env );
		assertThat( e, is( StringFunctor.class ) );
		e = obj.invoke( Selector.of( "." + Selector.OBJECT + ".text" ), env );
		assertThat( e, is( StringFunctor.class ) );
	}
}
