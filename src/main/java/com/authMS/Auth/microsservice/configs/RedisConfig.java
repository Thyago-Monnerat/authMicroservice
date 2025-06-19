package com.authMS.Auth.microsservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class RedisConfig {

    @Value("${redis.user}")
    private String redisAdmin;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public UnifiedJedis unifiedJedis() {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user(redisAdmin)
                .password(redisPassword)
                .build();
        HostAndPort hostAndPort = new HostAndPort("redis-19019.crce207.sa-east-1-2.ec2.redns.redis-cloud.com", 19019);

        return new UnifiedJedis(hostAndPort, config);
    }
}
