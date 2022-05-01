package org.formation.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.web.CreateOrderRequest;

import lombok.extern.java.Log;

@ApplicationScoped
@Log
public class OrderService {

	
	@Inject
	OrderRepository orderRepository;
	
	
	@Transactional
	public Order createOrder(CreateOrderRequest createOrderRequest) {
		Order order = createOrderRequest.createOrder();
		// Save in local DataBase
		orderRepository.persist(order);
			
		log.info("Order created ");
		
		return order;
	}
	
	
	
}
