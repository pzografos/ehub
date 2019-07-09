package com.tp.ehub.model.messaging;

public interface MessageRecord<K, M extends Message> {

	K getKey();

	M getMessage();

}
