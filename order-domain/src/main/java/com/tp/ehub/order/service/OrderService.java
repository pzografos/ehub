package com.tp.ehub.order.service;

import java.util.UUID;

import com.tp.ehub.order.model.Order;

public interface OrderService {

	Order placeOrder(Order order);

	void cancelOrder(UUID companyId, UUID orderId);

	void completeOrder(UUID companyId, UUID orderId);

}