package com.app.service;

import com.app.collection.Order;
import com.app.collection.Payment;
import com.app.external.PaymentService;
import com.app.repository.OrderRepository;
import com.app.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
//@Profile("dev")
public class OrderServiceImpl implements OrderService {
    private static int retryCount = 0;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RedisService redisService;

    @Override
    public Payment bookOrder(Order order) {
        logger.debug("Inside OrderServiceImpl bookOrder");
        Payment payment = null;

        if (order != null && order.getOrderId() != null) {
            // Creating Static Transaction Details in DB for payment collection.
            int transactionId = new Random(10000).nextInt();
            payment = Payment
                    .builder()
                    .paymentId("PAY_" + Math.random())
                    .transactionId("TXN_" + Math.random())
                    .message("Payment Successfully done for TransactionId:: " + "TXN_" + transactionId + " OrderId::" + order.getOrderId())
                    .orderId(order.getOrderId())
                    .status("Success")
                    .build();
            // Saving the order and payment details in DB
            paymentRepository.save(payment);
            orderRepository.save(order);
            // For Payment call payment service
            /*String paymentUrl = "http://localhost:8082/payments/doPayment/" + order.getOrderId();
            orderResponse = restTemplate.getForObject(paymentUrl, OrderResponse.class);
       */
        }
        // System.out.println("Payment Response::" + payment);
        logger.debug("Payment Response::" + payment);
        return payment;
    }

    @Override
    public Order findByOrderName(String orderName) {
        logger.debug("Inside OrderServiceImpl findByOrderName");
        //List<Order> orders = new ArrayList<>();
        Order order = redisService.get(orderName, Order.class);
        if (order != null) {
            logger.debug("Getting Data from Redis Service");
            //orders.add(order);
            return order;
        } else {
            Order orderDetails = orderRepository.findByOrderName(orderName);
            if (orderDetails != null) {
                logger.debug("Set Data for Redis Service");
                redisService.set(orderName, orderDetails, 300L);
            }
            return orderRepository.findByOrderName(orderName);
        }
    }

    @Override
    public void deleteById(Integer orderId) {
        logger.debug("Inside OrderServiceImpl deleteById");
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> findByPriceRange(double minPrice, double maxPrice) {
        logger.debug("Inside OrderServiceImpl findByPriceRange");
        return orderRepository.findOrderByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Payment> listOfTransactions() {

        logger.debug("Inside OrderServiceImpl listOfTransactions");
        return paymentService.getListOfPaymentDetails();
    }

    @Override
    public Order updateOrderByOrderId(Integer orderId, Order updateOrder) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        Order existingOrderNew = null;
        if (existingOrder.isPresent()) {
            existingOrderNew = existingOrder.get();
            existingOrderNew.setOrderName(updateOrder.getOrderName());
            existingOrderNew.setQty(updateOrder.getQty());
            existingOrderNew.setPrice(updateOrder.getPrice());
            existingOrderNew.setCategories(existingOrderNew.getCategories());
            orderRepository.save(existingOrderNew);
        }
        return existingOrderNew;
    }

    @Override
    public Order updateOrderByOrderIdPatch(Integer orderId, Order order) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        Order existingOrderNew = null;
        if (existingOrder.isPresent()) {
            existingOrderNew = existingOrder.get();
            existingOrderNew.setOrderName("New Red Label");
            existingOrderNew.setCategories(order.getCategories());
            orderRepository.save(existingOrderNew);
        }
        return existingOrderNew;
    }

    @Override
    public List<Order> findOrderByOrderIds(List<Integer> orderIds) {
        return orderRepository.findAllById(orderIds);
    }

    @Override
    //@Retryable(maxAttempts = 3)
    //@Retryable(value = {Exception.class}, backoff = @Backoff(delay = 1000))
    public int retryImplementation() throws Exception {
        retryCount++;
        logger.debug("Trying to perform operation, attempt: " + retryCount);
        if (retryCount < 3) {
            //throw new Exception("Simulated error occurred");
            logger.debug("Wait to up the service ..." + retryCount);
        }
        logger.debug("Operation performed successfully");
        return retryCount;
    }
}
