package com.tp.ehub.product.repository;

import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.repository.message.AbstractMessageStore;

@Dependent
public class ProductEventsStore extends AbstractMessageStore<UUID, ProductEvent>{

}
