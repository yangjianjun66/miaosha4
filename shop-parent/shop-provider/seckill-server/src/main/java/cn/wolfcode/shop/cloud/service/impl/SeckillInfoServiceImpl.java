package cn.wolfcode.shop.cloud.service.impl;

import cn.wolfcode.shop.cloud.common.BussinessException;
import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.mapper.OrderInfoMapper;
import cn.wolfcode.shop.cloud.msg.SeckillCodeMsg;
import cn.wolfcode.shop.cloud.service.ISeckillInfoService;
import cn.wolfcode.shop.cloud.service.ISeckillOrderSevice;
import cn.wolfcode.shop.cloud.service.ISeckillService;
import cn.wolfcode.shop.cloud.util.IdGenerateUtil;
import cn.wolfcode.shop.cloud.web.vo.SeckillGoodVo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SeckillInfoServiceImpl implements ISeckillInfoService {
    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private ISeckillOrderSevice seckillOrderSevice;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Override
    @Transactional//使用事务包装原子性
    public String doSeckill(Long userId, Long seckillId) {
        int count = seckillService.doSeckill(seckillId);
        String orderNo = this.createOrderInfo(userId,seckillId);
        seckillOrderSevice.createSeckillOrder(userId,seckillId,orderNo);
        return orderNo;
    }

    @Override
    public void cancelOrder(Long seckillId, String orderNo) {
        //先找到订单
        OrderInfo orderInfo = orderInfoMapper.find(orderNo);
        if (orderInfo.getStatus().equals(OrderInfo.STATUS_ARREARAGE)){
            int count = orderInfoMapper.updateCancelStatus(orderNo,orderInfo.STATUS_TIMEOUT);
            if (count == 0 ){
                throw new BussinessException(SeckillCodeMsg.CANCEL_ORDER_ERROR);
            }
            seckillService.incrStock(seckillId);
            seckillService.syncRedisStockCount(seckillId);
        }
    }

    private String createOrderInfo(Long userId, Long seckillId) {
        OrderInfo orderInfo = new OrderInfo();
        SeckillGoodVo seckillGoodVo = seckillService.find(seckillId);
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(null);
        orderInfo.setGoodCount(1);
        orderInfo.setGoodId(seckillGoodVo.getGoodId());
        orderInfo.setGoodImg(seckillGoodVo.getGoodImg());
        orderInfo.setGoodName(seckillGoodVo.getGoodName());
        orderInfo.setGoodPrice(seckillGoodVo.getGoodPrice());
        orderInfo.setSeckillPrice(seckillGoodVo.getSeckillPrice());
        orderInfo.setUserId(userId);
        orderInfo.setOrderNo(IdGenerateUtil.get().nextId() + "");
        orderInfoMapper.insert(orderInfo);
        return orderInfo.getOrderNo();
    }
}
