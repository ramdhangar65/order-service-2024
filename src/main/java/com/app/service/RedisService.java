package com.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null)
                return null;
            // Convert the object directly to the specified class
            if (entityClass.isInstance(o)) {
                return entityClass.cast(o);
            }
            // If the object is not of the specified class, attempt deserialization
            //String s = objectMapper.writeValueAsString(o);
            return objectMapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            logger.error("Exception{}", e.getMessage());
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            logger.debug("Get Value :::" + redisTemplate.opsForValue().get(key));
        } catch (RedisConnectionFailureException e1) {
            logger.error("Unable to connect to Redis. Please check your Redis configuration{}", e1.getMessage());
        } catch (Exception e) {
            logger.error("Exception{}", e.getMessage());
        }
    }

}
