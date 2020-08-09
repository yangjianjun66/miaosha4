package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import cn.wolfcode.shop.cloud.service.ISeckillService;
import cn.wolfcode.shop.cloud.web.vo.SeckillGoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGood")
public class SeckillGoodController {
    @Autowired
    private ISeckillService seckillServicel;
    @Autowired
    private MyRedisTemplate myRedisTemplate;
    @RequestMapping("/query")
    public Result<List<SeckillGoodVo>> query(){
        return Result.success(seckillServicel.query());
    }
    @RequestMapping("/find")
    public Result<SeckillGoodVo> find(Long seckillId){
        return Result.success(seckillServicel.find(seckillId));
    }
    @RequestMapping("/initData")
    public Result<String> initData(){
        List<SeckillGoodVo> seckillGoodVoList = seckillServicel.query();
        System.out.println("SeckillGoodController的seckillGoodVoList为null");
        for (SeckillGoodVo vo : seckillGoodVoList) {
            myRedisTemplate.set(SeckillKeyPrefix.SECKILL_STOCK_COUNT,vo.getId()+"",vo.getStockCount());
            myRedisTemplate.hset(SeckillKeyPrefix.SECKILL_GOOD_HASH,"",vo.getId()+"",vo);
        }
        return Result.success("初始化成功");
    }
}
