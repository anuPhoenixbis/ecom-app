package com.app.ecom_app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final ObjectMapper objectMapper = new  ObjectMapper();

    private final RedisTemplate redisTemplate;

    public <T> T get(String key, TypeReference<T> typeReference) {
        try{
            String jsonValue = (String) redisTemplate.opsForValue().get(key);
            if (jsonValue == null) {
                return null;
            }
            return objectMapper.readValue(jsonValue, typeReference);
        }catch (Exception e){
            log.error("Redis GET failed for key: {}", key, e);
            return null;
        }
    }

    public void set(String key, Object obj, Long ttl) {
        try{
            String jsonValue = objectMapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public <T> T get(String key, Class<T> entityClass) {
        try{
            String jsonValue = (String) redisTemplate.opsForValue().get(key);
            if (jsonValue == null) {
                return null;
            }
            return objectMapper.readValue(jsonValue,entityClass);
        }catch (Exception e){
            log.error("Redis GET failed for key: {}", key, e);
            return null;
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
    public void deleteAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
