package com.app.service;

import com.app.collection.Order;
import com.app.collection.Payment;
import com.app.dto.OrderIdRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    Payment bookOrder(Order order);

    //List<Order> findByOrderName(String orderName);

   Order findByOrderName(String orderName);

    void deleteById(Integer orderId);

    List<Order> findByPriceRange(double minPrice, double maxPrice);

    List<Payment> listOfTransactions();

    Order updateOrderByOrderId(Integer orderId, Order order);

    Order updateOrderByOrderIdPatch(Integer orderId, Order order);

    List<Order> findOrderByOrderIds(List<Integer> orderIds);

    int retryImplementation() throws Exception;

    //List<Order> findOrderByOrderCategories(List<String> keys);
}
