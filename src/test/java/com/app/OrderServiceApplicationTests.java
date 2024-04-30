package com.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void setRedisTemplate() {
        redisTemplate.opsForValue().set("email","ramdhangar@gmail.com");
        Object email = redisTemplate.opsForValue().get("port");
        System.out.println("email::"+email);
    }

}
