package com.app.repository;

import com.app.collection.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, Integer> {

    List<Order> findByOrderName(String orderName);

    /* List<Order> findOrderByPriceBetween(double minPrice,double maxPrice);*/
    @Query("{'price' : { $gte: ?0, $lte: ?1 } }")
    List<Order> findOrderByPriceBetween(double minPrice, double maxPrice);
}
