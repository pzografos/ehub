package com.tp.ehub.command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tp.ehub.command.model.messaging.kafka.CommandsTopic;

public class CreateProductCommandTest {
	
	private CommandsTopic topic = CommandsTopic.get();

	@DisplayName("Serialization")
	@Test
	void testSerialization() {
		
		ZonedDateTime now = ZonedDateTime.now();
		
		CreateProductCommand command = new CreateProductCommand();
		command.setCompanyId(UUID.randomUUID());
		command.setCode("1234");
		command.setDescription("very good product");
		command.setName("theProduct");
		command.setQuantity(2L);
		command.setTimestamp(now);
		
		byte[] commandAsBytes = topic.getValueSerializer().apply(command);
		
		System.out.println(new String(commandAsBytes));
		Command unMarshalledCommand = topic.getValueDeserializer().apply(commandAsBytes);
		
		assertThat(unMarshalledCommand, instanceOf(CreateProductCommand.class));
		
		CreateProductCommand actual = (CreateProductCommand) unMarshalledCommand;
		
		assertEquals(command.getCompanyId(), actual.getCompanyId());
		assertEquals(command.getCode(), actual.getCode());
		assertEquals(command.getDescription(), actual.getDescription());
		assertEquals(command.getName(), actual.getName());
		assertEquals(command.getQuantity(), actual.getQuantity());
		assertEquals(command.getTimestamp().toEpochSecond(), actual.getTimestamp().toEpochSecond());

	}
}
