package de.jbee.panda;

import static de.jbee.panda.Expr.expr;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestExpr {

	@Test
	public void testExprUntilWhitespace_MatchesNothingCase() {
		Expr expr = expr( " some" );
		assertThat( expr.untilWhitespace(), is( Expr.EMPTY ) );
		assertThat( expr, is( expr( " some" ) ) );
	}

	@Test
	public void testExprUntilWhitespace_MatchesEverythingCase() {
		Expr expr = expr( "some" );
		assertThat( expr.untilWhitespace(), is( expr( "some" ) ) );
		assertThat( expr, is( Expr.EMPTY ) );
	}

	@Test
	public void testExprUntilWhitespace_MatchesSomethingCase() {
		Expr expr = expr( "some thing" );
		assertThat( expr.untilWhitespace(), is( expr( "some" ) ) );
		assertThat( expr, is( expr( " thing" ) ) );
	}

}
