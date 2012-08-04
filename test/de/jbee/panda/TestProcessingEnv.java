package de.jbee.panda;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestProcessingEnv {

	@Test
	public void testEnv_EachCase() {
		ProcessingEnv env = new Environment( new Context() );
		FunctorizeEnv func = env.functorize();
		Var foo = Var.named( "foo" );
		String[] values = new String[] { "a", "b", "c" };
		env.context().define( foo, func.behaviour( "each", values ) );
		int i = 0;
		while ( !env.context().processed( env ) && i < values.length ) {
			Functor each = env.value( foo );
			Functor e = each.invoke( Expr.EMPTY, env );
			assertThat( e.text(), is( values[i] ) );
			i++;
		}
		assertThat( i, is( values.length ) );
	}
}
