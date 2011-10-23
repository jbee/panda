package de.jbee.panda.functor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.panda.Context;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.FunctorizeEnv;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Expr;

public class TestFunctorizer {

	static class ObjectType {

		private final Integer height = 1;
		int width = 2;
		public String name = "peter";
		Date moment = new Date( 2011, 10, 1 );
	}

	@Test
	public void testYieldIntegerFunctor() {
		FunctorizeEnv f = Functorize.getInstance();
		assertThat( f.value( 1 ), is( IntegerFunctor.class ) );
		assertThat( f.value( 1L ), is( IntegerFunctor.class ) );
		assertThat( f.behaviour( Functorizer.NUMBER, 2.3 ), is( IntegerFunctor.class ) );
	}

	@Test
	public void testYieldStringFunctor() {
		FunctorizeEnv f = Functorize.getInstance();
		assertThat( f.value( "test" ), is( StringFunctor.class ) );
		assertThat( f.value( "test".toCharArray() ), is( StringFunctor.class ) );
		assertThat( f.behaviour( Functorizer.TEXT, "abc" ), is( StringFunctor.class ) );
	}

	@Test
	public void testYieldListFunctor() {
		FunctorizeEnv f = Functorize.getInstance();
		assertThat( f.behaviour( Functorizer.LIST, List.with.noElements() ), is( ListFunctor.class ) );
		assertThat( f.behaviour( Functorizer.LIST, List.with.elements( 1, 2 ) ),
				is( ListFunctor.class ) );
	}

	@Test
	public void testYieldObjectFunctor() {
		ProcessingEnv env = new Environment( new Context() );
		Functor obj = env.functorize().value( new ObjectType() );
		assertThat( obj, is( ObjectFunctor.class ) );
		Functor e = obj.invoke( Expr.expr( ".height" ), env );
		assertThat( e, is( IntegerFunctor.class ) );
		e = obj.invoke( Expr.expr( ".width" ), env );
		assertThat( e, is( IntegerFunctor.class ) );
		e = obj.invoke( Expr.expr( ".name" ), env );
		assertThat( e, is( StringFunctor.class ) );
		e = obj.invoke( Expr.expr( ".moment" ), env );
		assertThat( e, is( ObjectFunctor.class ) );
		e = e.invoke( Expr.expr( ".fastTime" ), env );
		assertThat( e, is( IntegerFunctor.class ) );
	}

}
