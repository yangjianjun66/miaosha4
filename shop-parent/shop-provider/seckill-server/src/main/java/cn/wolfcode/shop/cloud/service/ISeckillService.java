package cn.wolfcode.shop.cloud.service;

import cn.wolfcode.shop.cloud.web.vo.SeckillGoodVo;

import java.util.List;

public interface ISeckillService {
    List<SeckillGoodVo> query();

    SeckillGoodVo find(Long seckillId);

    List<SeckillGoodVo> queryByCache();

    SeckillGoodVo findByCache(Long seckillId);

    int doSeckill(Long seckillId);

    void syncRedisStockCount(Long seckillId);

    void incrStock(Long seckillId);
}
