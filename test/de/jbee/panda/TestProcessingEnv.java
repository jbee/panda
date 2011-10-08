package de.jbee.panda;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestProcessingEnv {

	@Test
	public void testEnv() {
		ProcessingEnv env = new Environment( new Context() );
		Functorizer func = env.functorize();
		Var foo = Var.named( "foo" );
		String[] values = new String[] { "a", "b", "c" };
		env.context().bind( foo, func.behaviour( "each", func.value( values ) ) );
		int i = 0;
		env.context().bind( env );
		while ( !env.context().independent( env ) && i < values.length ) {
			Functor each = env.value( foo );
			Functor e = each.invoke( Selector.EMPTY, env );
			String value = e.text( env );
			System.out.println( value );
			assertThat( value, is( values[i] ) );
			env.context().rebind( env );
			i++;
		}
		assertThat( i, is( values.length ) );
	}
}
