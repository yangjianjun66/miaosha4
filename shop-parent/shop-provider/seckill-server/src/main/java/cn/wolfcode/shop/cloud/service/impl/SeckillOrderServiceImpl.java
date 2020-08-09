package cn.wolfcode.shop.cloud.service.impl;

import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.domain.SeckillOrder;
import cn.wolfcode.shop.cloud.mapper.SeckillOrderMapper;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import cn.wolfcode.shop.cloud.service.ISeckillOrderSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeckillOrderServiceImpl implements ISeckillOrderSevice {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private MyRedisTemplate myRedisTemplate;
    @Override
    public SeckillOrder findByUserIdAndSeckillId(Long userId, Long seckillId) {
        return seckillOrderMapper.finByUserIdAndSeckillId(userId,seckillId);
    }

    @Override
    public void createSeckillOrder(Long userId, Long seckillId, String orderNo) {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderNo(orderNo);
        seckillOrder.setUserId(userId);
        seckillOrder.setSeckillId(seckillId);
        seckillOrderMapper.insert(seckillOrder);
        myRedisTemplate.set(SeckillKeyPrefix.SECKILL_ORDER,seckillId+":"+userId,true);
    }

    @Override
    public OrderInfo find(String orderNo) {
        return seckillOrderMapper.find(orderNo);
    }

    @Override
    public int changePayStatus(String orderNo, Integer status) {
        return seckillOrderMapper.changePayStatus(orderNo,status);
    }

}
