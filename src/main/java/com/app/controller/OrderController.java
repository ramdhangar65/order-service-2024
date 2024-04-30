package com.app.controller;

import com.app.collection.Order;
import com.app.collection.Payment;
import com.app.dto.OrderIdRequest;
import com.app.dto.OrderResponse;
import com.app.service.OrderService;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;
    int i = 0;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;

    @GetMapping("/welcome-msg")
    public String paymentMessage() {
        logger.info("Welcome Message from Order Service...");
        return "Welcome Message from Order Service...";
    }


    @PostMapping("/book")
    public Payment bookOrder(@RequestBody Order order) {
        logger.debug("Inside Order Controller bookOrder");
        return orderService.bookOrder(order);
    }

    @GetMapping("/{orderName}")
    //public List<Order> findByOrderName(@PathVariable String orderName)
    public Order findByOrderName(@PathVariable String orderName) {
        logger.debug("Inside Order Controller findByOrderName");
        return orderService.findByOrderName(orderName);
    }

    @DeleteMapping
    public void delete(@RequestParam Integer orderId) {
        logger.debug("Inside Order Controller delete");
        orderService.deleteById(orderId);
    }

    @GetMapping("/price")
    public List<Order> findByPriceRange(@RequestParam double minPrice,
                                        @RequestParam double maxPrice) {
        logger.debug("Inside Order Controller findByPriceRange");
        return orderService.findByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("payments/paymentDetails")
    public List<Payment> listOfTransactions() {
        logger.debug("Inside Order Controller listOfTransactions");
        return orderService.listOfTransactions();
    }

    @PutMapping("{orderId}")
    public Order updateOrderByOrderId(@PathVariable Integer orderId, @RequestBody Order order) {
        logger.debug("Inside updateOrderByOrderId Controller....");
        return orderService.updateOrderByOrderId(orderId, order);
    }

    @PatchMapping("{orderId}")
    public Order updateOrderByOrderIdPatch(@RequestParam Integer orderId, @RequestBody Order order) {
        logger.debug("Inside updateOrderByOrderIdPatch Controller....");
        return orderService.updateOrderByOrderIdPatch(orderId, order);
    }

    @PostMapping("/findOrdersByIds")
    public List<Order> findAllByIdIn(@RequestBody OrderIdRequest orderIds) {
        List<Integer> orderIds1 = orderIds.getOrderIds();
        logger.debug("Inside Order Controller findByOrderName");
        return orderService.findOrderByOrderIds(orderIds1);
    }

    long lastCallTime = 0l;
    long timeDifference = 0l;

    @GetMapping("/retryMessage")
    //@Retryable
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String retryMessage() throws Exception {
        timeDifference = System.currentTimeMillis() - lastCallTime;
        logger.info(++i + ":: retryImplementation from Order Service..." + timeDifference);
        lastCallTime = System.currentTimeMillis();
        restTemplate.getForObject("http://localhost:8082/payments/paymentDetails", Object.class);
        // int count = orderService.retryImplementation();
        return "Message received Successfully ...";
    }
    @Recover
    public String getRecoveryMessage() {
        return "Recovery Message ...";
    }
}
