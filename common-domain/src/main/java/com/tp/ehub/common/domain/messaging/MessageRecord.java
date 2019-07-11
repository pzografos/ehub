package com.tp.ehub.common.domain.messaging;

public interface MessageRecord<K, M extends Message> {

	K getKey();

	M getMessage();

}
