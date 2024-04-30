package com.app.controller;

import com.app.collection.Payment;
import com.app.service.RedisCacheService;
import org.bouncycastle.pqc.legacy.crypto.rainbow.Layer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableCaching
public class RedisCacheController {

    public  static  final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @GetMapping("/set/{key}/{value}")
    public String setValue(@PathVariable String key, @PathVariable String value) {
        redisCacheService.set(key, value);
        return "Key-value pair set successfully";
    }

    @GetMapping("/get/{key}")
    public String getValue(@PathVariable String key) {
        logger.debug("Redis Get Key::{}",key);
        return redisCacheService.get(key);
    }

    @GetMapping("payments/paymentDetails")
    public List<Payment> listOfTransactions() {
        logger.debug("Inside Order Controller listOfTransactions");
        return null;
    }
}
