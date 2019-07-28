package com.tp.ehub.common.domain.messaging.container;

import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.common.domain.messaging.Message;

public abstract class AbstractKeyValueMessageContainer<K, M extends Message<K>> implements KeyValueMessageContainer<K, M>{

	@Inject
	@Named("objectMapper")
	protected ObjectMapper mapper;
	
	protected String name;

	protected Class<K> keyClass;

	protected Class<M> messageClass;

	protected AbstractKeyValueMessageContainer(String name, Class<K> keyClass, Class<M> messageClass) {
		this.name = name;
		this.keyClass = keyClass;
		this.messageClass = messageClass;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Class<M> getMessageClass(){
		return this.messageClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Function<String, K> getKeyDeserializer() {
		if (keyClass.equals(String.class)) {
			return s -> (K) s;
		}

		Method keyReaderMethod = StringCreatorExtractor.stringCreator(keyClass);

		if (keyReaderMethod == null) {
			return null;
		}

		return keyValue -> {
			try {
				return (K) keyReaderMethod.invoke(null, keyValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}

	@Override
	public Function<K, String> getKeySerializer() {
		return s -> s.toString();
	}

	@Override
	public Function<byte[], M> getValueDeserializer() {
		return message -> {
			try {
				return mapper.readValue(message, messageClass);
			} catch (IOException e) {
				throw new IllegalArgumentException(format("Failed to deserialize message from `%s`", new String(message)), e);
			}
		};
	}

	@Override
	public Function<M, byte[]> getValueSerializer() {
		return message -> {
			try {
				return mapper.writeValueAsBytes(message);
			} catch (IOException e) {
				throw new IllegalArgumentException(format("Failed to serialize message `%s`", message), e);
			}
		};
	}

	@Override
	public Function<M, String> getPartitionSelector(){
		return message -> message.getKey().toString();
	}

	@Override
	public int getPartitions() {
		return 15;
	}

}