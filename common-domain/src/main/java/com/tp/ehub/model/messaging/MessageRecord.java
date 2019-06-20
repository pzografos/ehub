package com.tp.ehub.model.messaging;

public interface MessageRecord<K, M extends Message>{
	
	public K getKey();
	
	public M getMessage();
	
}
