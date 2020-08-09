package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.msg.SeckillCodeMsg;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.RedisValue;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/path")
public class PathController {
    @Autowired
    private MyRedisTemplate myRedisTemplate;

    @RequestMapping("getPath")
    public Result<String> getPath(String uuid,Integer verifyCode, @RedisValue User user){
        if (user == null){
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        Integer result = myRedisTemplate.get(SeckillKeyPrefix.VERIFY_CODE_RESULT, uuid, Integer.class);
        if (result==null||!result.equals(verifyCode)){
            return Result.error(SeckillCodeMsg.VERIFYCODE_ERROR);
        }
        String path = UUID.randomUUID().toString().replace("-", "");
        myRedisTemplate.set(SeckillKeyPrefix.SECKILL_PATH,uuid,path);
        return Result.success(path);
    }
}
