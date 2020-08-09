package cn.wolfcode.shop.cloud.service;

public interface ISeckillInfoService {
    String doSeckill(Long userId, Long seckillId);

    void cancelOrder(Long seckillId, String orderNo);
}
