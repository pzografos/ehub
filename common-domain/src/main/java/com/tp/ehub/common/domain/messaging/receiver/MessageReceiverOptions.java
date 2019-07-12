package com.tp.ehub.common.domain.messaging.receiver;

public class MessageReceiverOptions {

	private final String consumerId;
	private final boolean fromStart;

	public MessageReceiverOptions(String consumerId) {
		this.consumerId = consumerId;
		this.fromStart = false;
	}

	public MessageReceiverOptions(String consumerId, boolean fromStart) {
		this.consumerId = consumerId;
		this.fromStart = fromStart;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public boolean isFromStart() {
		return fromStart;
	}

}
