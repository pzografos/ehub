package com.tp.ehub.common.domain.messaging.receiver;

public class MessageReceiverOptions {

	private String consumerId;
	private String partitionSelector;
	private boolean fromStart;

	public MessageReceiverOptions(String consumerId) {
		this.consumerId = consumerId;
	}

	public MessageReceiverOptions(String consumerId, boolean fromStart) {
		this(consumerId);
		this.fromStart = fromStart;
	}

	private void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public void setFromStart(boolean fromStart) {
		this.fromStart = fromStart;
	}

	public String getPartitionSelector() {
		return partitionSelector;
	}

	public void setPartitionSelector(String partitionSelector) {
		this.partitionSelector = partitionSelector;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public boolean isFromStart() {
		return fromStart;
	}

}
