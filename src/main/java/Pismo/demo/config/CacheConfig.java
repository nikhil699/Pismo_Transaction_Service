package Pismo.demo.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        var valueSerializer = new GenericJackson2JsonRedisSerializer();
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .prefixCacheNameWith("pismo:")              // keys like pismo:accounts::1
                .entryTtl(Duration.ofMinutes(10));          // default TTL

        // Optional per-cache TTLs
        var accountCfg = defaultConfig.entryTtl(Duration.ofMinutes(30));
        var txCfg = defaultConfig.entryTtl(Duration.ofMinutes(5));

        return RedisCacheManager.builder(cf)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("accounts", accountCfg)
                .withCacheConfiguration("accountTxPage", txCfg)
                .build();
    }

    // Optional custom key generator if needed
    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
