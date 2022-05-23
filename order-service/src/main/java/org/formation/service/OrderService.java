package org.formation.service;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.event.OrderEvent;
import org.formation.web.CreateOrderRequest;

import lombok.extern.java.Log;

@ApplicationScoped
@Log
public class OrderService {

	
	@Inject
	OrderRepository orderRepository;
	
	@Channel("order")
	Emitter<OrderEvent> orderEventEmitter;
	
	@Transactional
	public Order createOrder(CreateOrderRequest createOrderRequest) {
		Order order = createOrderRequest.createOrder();
		// Save in local DataBase
		orderRepository.persist(order);
			
		log.info("Order created ");
		
		return order;
	}
	
	public void publishEvent(OrderEvent orderEvent) {
		CompletionStage<Void> ack = orderEventEmitter.send(orderEvent);
		ack.thenRun(() -> System.out.println("Message Acked"));
	}
	
}
