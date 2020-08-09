package cn.wolfcode.shop.cloud.service;

import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.domain.SeckillOrder;

public interface ISeckillOrderSevice {
    SeckillOrder findByUserIdAndSeckillId(Long userId,Long seckillId);

    void createSeckillOrder(Long userId, Long seckillId, String orderNo);

    OrderInfo find(String orderNo);

    int changePayStatus(String out_trade_no, Integer statusAccountPaid);
}
