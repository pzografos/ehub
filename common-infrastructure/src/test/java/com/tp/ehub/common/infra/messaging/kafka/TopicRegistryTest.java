package com.tp.ehub.common.infra.messaging.kafka;

import java.util.UUID;

import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.infra.messaging.kafka.container.TopicRegistry;
import com.tp.ehub.common.infra.producer.ObjectMapperProducer;

@EnableWeld
public class TopicRegistryTest {

    @WeldSetup 
    public WeldInitiator weld = WeldInitiator.of(TopicRegistry.class, ObjectMapperProducer.class);
    
	@Inject
	TopicRegistry topicRegistry;

	@Test
	void testSimpleMessage() {
		MessageContainer<UUID, SimpleMessage> topic = topicRegistry.get(SimpleMessage.class);
		Assertions.assertEquals("simple-message-topic", topic.getName());
		Assertions.assertEquals(SimpleMessage.class, topic.getMessageClass());
	}
	
	@Test
	void testAnnotationInheritence() {
		MessageContainer<UUID, SubClassMessage> topic = topicRegistry.get(SubClassMessage.class);
		Assertions.assertEquals("base-message-topic", topic.getName());
		Assertions.assertEquals(SubClassMessage.class, topic.getMessageClass());
	}
	
	@Test()
	void testNotAnnotatedMessage() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			topicRegistry.get(NotAnnotatedSimpleMessage.class);;
		  });		
	}
}
