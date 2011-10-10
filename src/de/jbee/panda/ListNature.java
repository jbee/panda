package de.jbee.panda;

import de.jbee.lang.List;

public interface ListNature
		extends Nature {

	List<? extends Functor> elements();
}
