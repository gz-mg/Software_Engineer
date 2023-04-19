package com.edu.hzau.cocs.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author yue
 */

@Slf4j
@Service
public class RedisService {
    private static final int EXPIRE_TIME = 30;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 读取数据
     */
    public String getString(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入数据
     */
    public void setString(final String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value, EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("set cache failed.");
            e.printStackTrace();
        }
    }
}
