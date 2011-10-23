package de.jbee.panda;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * A bunch of tests for the {@link ProcessingEnv#eval(Expr)}-method.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public class TestEval {

	private ProcessingEnv env;

	@Before
	public void setUp() {
		env = new Environment( new Context() );
	}

	@Test
	public void testExistsingVarReferenceCase() {
		Var foo = Var.named( "foo" );
		env.define( foo, env.functorize().value( 10 ) );
		Functor value = env.eval( Expr.ref( "foo" ) );
		assertThat( value, instanceOf( IntegralNature.class ) );
		assertThat( ( (IntegralNature) value ).integer(), is( 10 ) );
	}

	@Test
	public void testNonExistingVarReferenceCase() {
		Functor value = env.eval( Expr.expr( "@foo" ) );
		assertThat( value, instanceOf( NothingNature.class ) );
		assertThat( value.is(), is( false ) );
	}

	@Test
	public void testNothingQuestionmarkIsFalse() {
		Functor value = env.eval( Expr.expr( "not @foo?" ) );
		assertThat( value, instanceOf( PredicateNature.class ) );
		assertThat( value.is(), is( true ) );
	}
}
