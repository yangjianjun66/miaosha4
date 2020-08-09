package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.domain.SeckillOrder;
import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.mq.MQConstants;
import cn.wolfcode.shop.cloud.mq.OrderMessage;
import cn.wolfcode.shop.cloud.msg.SeckillCodeMsg;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.RedisValue;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import cn.wolfcode.shop.cloud.service.ISeckillInfoService;
import cn.wolfcode.shop.cloud.service.ISeckillOrderSevice;
import cn.wolfcode.shop.cloud.service.ISeckillService;
import cn.wolfcode.shop.cloud.web.vo.SeckillGoodVo;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/order")
public class OrderInfoController {
    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private ISeckillOrderSevice seckillOrderSevice;
    @Autowired
    private ISeckillInfoService seckillInfoService;
    @Autowired
    private MyRedisTemplate myRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static ConcurrentHashMap<Long,Boolean> isStockOverMap = new ConcurrentHashMap<>();


    @RequestMapping("/{path}/doSeckill")
    public Result<String> doSeckill(Long seckillId,String uuid,@PathVariable(name = "path") String path, @RedisValue User user){
        //判断是否是登录了的
        if (user == null){
            return Result.error(SeckillCodeMsg.LOGIN_TIMEOUT);
        }
        //判断参数的问题
        if (seckillId == null || seckillId<=0){
            System.out.println("seckillId == null || seckillId<=0");
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        //判断地址是否是跟传过来的一样
        String oldPath = myRedisTemplate.get(SeckillKeyPrefix.SECKILL_PATH,uuid,String.class);
        if (StringUtils.isEmpty(oldPath)||!oldPath.equals(path)){
            return Result.error(SeckillCodeMsg.SECKILL_STOCK_OVER);
        }
        //判断是否是有库存
        Boolean isStockOverSign = isStockOverMap.get(seckillId);
        if (isStockOverSign!=null&&isStockOverSign){
            return Result.error(SeckillCodeMsg.SECKILL_STOCK_OVER);
        }
        //根据seckillId查询出对应的商品
        SeckillGoodVo seckillGoodVo = seckillService.findByCache(seckillId);
        //这里的判断是因为要是正规的从浏览器上去操作的话都 是会带上id去查询出对应的商品的.如果的null 的话就代表着是别人很有可能是通过postman去瞎几把查的
        if (seckillGoodVo == null){
            System.out.println("seckillGoodVo是null");
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }

        //秒杀的时间
        Date now = new Date();
        if (now.getTime()<seckillGoodVo.getStartDate().getTime() || now.getTime()>seckillGoodVo.getEndDate().getTime()){
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        //判断用户是否是有抢购过这个商品了,并且进行一些操作
        SeckillOrder seckillOrder = seckillOrderSevice.findByUserIdAndSeckillId(user.getId(), seckillId);
        if (myRedisTemplate.exists(SeckillKeyPrefix.SECKILL_ORDER,seckillId+":"+user.getId())){
            return Result.error(SeckillCodeMsg.REPEAT_SECKILL);
        }
        //秒杀的动作
        /*Long count = myRedisTemplate.decr(SeckillKeyPrefix.SECKILL_STOCK_COUNT, seckillId + "");
        if (count<0){
            isStockOverMap.put(seckillId,true);
            return Result.error(SeckillCodeMsg.SECKILL_STOCK_OVER);
        }
        String oederNo = seckillInfoService.doSeckill(user.getId(),seckillId);*/
        OrderMessage orderMessage = new OrderMessage(user.getId(),seckillId, uuid);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_PEDDING_QUEUE,orderMessage);
        return Result.success("进入队列成功,请等候结果",null);
    }

    @RequestMapping("/find")
    public Result<OrderInfo> find(String orderNo,@RedisValue User user){
        if (StringUtils.isEmpty(orderNo)){
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        if (user == null){
            return Result.error(SeckillCodeMsg.LOGIN_TIMEOUT);
        }
        OrderInfo orderInfo = seckillOrderSevice.find(orderNo);

        if (orderInfo == null || !orderInfo.getUserId().equals(user.getId())){
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        return Result.success(orderInfo);
    }
}
