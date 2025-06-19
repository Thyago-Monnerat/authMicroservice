package com.authMS.Auth.microsservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final UnifiedJedis unifiedJedis;

    public void blackListToken(String jti, Long ttl) {
        unifiedJedis.setex(jti, ttl, "true");
    }

    public boolean checkTokenExistence(String jti){
        return unifiedJedis.exists(jti);
    }
}
