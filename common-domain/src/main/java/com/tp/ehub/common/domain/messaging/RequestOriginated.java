package com.tp.ehub.common.domain.messaging;

import java.util.UUID;

public interface RequestOriginated {

	/**
	 * @return the request ID that started the chain of events that lead to the
	 *         current message
	 */
	UUID getRequestId();
}
