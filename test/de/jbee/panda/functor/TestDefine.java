package de.jbee.panda.functor;

import org.junit.Test;

import de.jbee.panda.Context;
import de.jbee.panda.Environment;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Var;

public class TestDefine {

	@Test
	public void testDefineVarFunctor() {
		ProcessingEnv env = new Environment( new Context() );
		env.define( Var.named( "__1__" ), env.functorize().behaviour( Functorizer.DEF,
				"var bar as true" ) );
		env.context().processed( env );

	}
}
