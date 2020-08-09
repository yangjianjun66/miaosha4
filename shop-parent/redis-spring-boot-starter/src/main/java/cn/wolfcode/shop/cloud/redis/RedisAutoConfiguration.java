package cn.wolfcode.shop.cloud.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnClass({JedisPool.class, Jedis.class})
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "redis",name = "host")
public class RedisAutoConfiguration {
    @Autowired
    private RedisProperties redisConfig;
    @Bean
    public JedisPool pool() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getPoolMaxTotal());
        config.setMaxIdle(redisConfig.getPoolMaxIdle());
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);
        JedisPool jedisPool = new JedisPool(config,redisConfig.getHost(),redisConfig.getPort(),redisConfig.getTimeout(),redisConfig.getPassword());
        return jedisPool;
    }
    @Bean
    public MyRedisTemplate redisService(){
        return new MyRedisTemplate();
    }
}
