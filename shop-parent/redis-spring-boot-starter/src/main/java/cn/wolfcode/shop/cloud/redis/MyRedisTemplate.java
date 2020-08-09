package cn.wolfcode.shop.cloud.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class MyRedisTemplate {

    @Autowired
    private JedisPool jedisPool;

    //设置redis
    public <T> void set(KeyPrefix keyPrefix,String key,T data){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            if (keyPrefix.getExprieSeconds()>0){
                jedis.setex(realKey,keyPrefix.getExprieSeconds(), JSON.toJSONString(data));
            }else {
                jedis.set(realKey,JSON.toJSONString(data));
            }
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //查询redis
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            String objStr = jedis.get(realKey);
            /*if (StringUtils.isEmpty(objStr)){
                return null;
            }*/
            return JSON.parseObject(objStr,clazz);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    //判断有没有这个redis
    public boolean exists(KeyPrefix keyPrefix,String key){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    //重新设置过效时间
    public Long expire(KeyPrefix keyPrefix,String key,int expireSeconds){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.expire(realKey,expireSeconds);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //自增
    public Long incr(KeyPrefix keyPrefix,String key){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //自减
    public Long decr(KeyPrefix keyPrefix,String key){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //hset
    public <T> void  hset(KeyPrefix keyPrefix,String key,String field,T data){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            jedis.hset(realKey,field,JSON.toJSONString(data));
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //hget
    public <T> T hget(KeyPrefix keyPrefix,String key,String field,Class<T> clazz){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            String objStr = jedis.hget(realKey,field);
            if (objStr == null){
                return null;
            }
            return JSON.parseObject(objStr,clazz);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    //getAll
    public <T> Map<String,T> hgetAll(KeyPrefix keyPrefix,String key,Class<T> clazz){
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = keyPrefix.getPrefix() + key;
            Map<String,String> map = jedis.hgetAll(realKey);
            if (map == null){
                return null;
            }
            Map<String,T> resultMap = new HashMap<>();
            for (Map.Entry<String,String> entry:map.entrySet()){
                resultMap.put(entry.getKey(),JSON.parseObject(entry.getValue(),clazz));
            }
            return resultMap;
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

}
