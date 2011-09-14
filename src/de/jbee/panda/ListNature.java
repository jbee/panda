package de.jbee.panda;

import de.jbee.lang.List;

public interface ListNature
		extends Nature {

	List<Functor> elements( EvaluationEnv env );
}
