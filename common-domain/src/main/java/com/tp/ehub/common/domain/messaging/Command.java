package com.tp.ehub.common.domain.messaging;

import com.tp.ehub.common.domain.request.RequestOriginated;

/**
 * The <code>Command</code> represents a trigger for the system. In its most
 * common form, it represents a user request.
 *
 */
public interface Command<K> extends Message<K>, RequestOriginated{
		
}
