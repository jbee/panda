package de.jbee.panda.functor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.panda.Context;
import de.jbee.panda.Environment;
import de.jbee.panda.Functor;
import de.jbee.panda.Functorizer;
import de.jbee.panda.ProcessingEnv;
import de.jbee.panda.Var;

public class TestDefine {

	@Test
	public void testDefineVarAsTrue() {
		ProcessingEnv env = new Environment( new Context() );
		env.define( Var.defined( 1 ), env.functorize().behaviour( Functorizer.DEF,
				"var bar as true" ) );
		env.context().processed( env );
		Functor value = env.value( Var.named( "bar" ) );
		assertThat( value, instanceOf( BooleanFunctor.class ) );
		assertThat( value.is(), is( true ) );
	}

	@Test
	public void testDefineCase() {
		ProcessingEnv env = new Environment( new Context() );
		Var var = Var.named( Functorizer.CASE );
		env.define( var, env.functorize().behaviour( Functorizer.DEF, "case true" ) );
		env.context().processed( env );
		Functor value = env.value( var );
		assertThat( value, instanceOf( CaseFunctor.class ) );
		assertThat( value.is(), is( true ) );
	}

	@Test
	public void testDefineCaseExists() {
		ProcessingEnv env = new Environment( new Context() );
		Var var = Var.named( Functorizer.CASE );
		env.define( Var.named( "foo" ), env.functorize().value( "bar" ) );
		env.define( var, env.functorize().behaviour( Functorizer.DEF, "case exists @foo" ) );
		env.context().processed( env );
		Functor value = env.value( var );
		assertThat( value, instanceOf( CaseFunctor.class ) );
		assertThat( value.is(), is( true ) );
	}

	@Test
	public void testDefineCaseNotExists() {
		ProcessingEnv env = new Environment( new Context() );
		Var var = Var.named( Functorizer.CASE );
		env.define( var, env.functorize().behaviour( Functorizer.DEF, "case not exists @foo" ) );
		env.context().processed( env );
		Functor value = env.value( var );
		assertThat( value, instanceOf( CaseFunctor.class ) );
		assertThat( value.is(), is( true ) );
	}

	@Test
	public void testDefineCaseNotNothing() {
		ProcessingEnv env = new Environment( new Context() );
		Var var = Var.named( Functorizer.CASE );
		env.define( var, env.functorize().behaviour( Functorizer.DEF, "case not @foo" ) );
		env.context().processed( env );
		Functor value = env.value( var );
		assertThat( value, instanceOf( CaseFunctor.class ) );
		assertThat( value.is(), is( false ) );
	}
}
