package de.jbee.panda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.jbee.panda.functor.TestDefine;
import de.jbee.panda.functor.TestFunctorizer;

@RunWith ( Suite.class )
@Suite.SuiteClasses ( { TestProcessingEnv.class, TestDefine.class, TestFunctorizer.class } )
public class PandaTestSuite {
	//nothing
}
