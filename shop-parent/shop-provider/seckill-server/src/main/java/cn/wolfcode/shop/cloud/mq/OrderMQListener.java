package cn.wolfcode.shop.cloud.mq;

import cn.wolfcode.shop.cloud.msg.SeckillCodeMsg;
import cn.wolfcode.shop.cloud.service.ISeckillInfoService;
import cn.wolfcode.shop.cloud.service.ISeckillService;
import cn.wolfcode.shop.cloud.web.controller.OrderInfoController;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderMQListener {
    @Autowired
    private ISeckillInfoService orderInfoService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ISeckillService seckillService;
    //异步下单
    @RabbitListener(queuesToDeclare = @Queue(name = MQConstants.ORDER_PEDDING_QUEUE))
    public void handleOrderPeddingQueue(@Payload OrderMessage orderMessage, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,Channel channel) throws IOException {
        Map<String,Object> param = new HashMap<>();
        param.put("uuid",orderMessage.getUuid());
        param.put("seckillId",orderMessage.getSeckillId());
        try {
            String orderNo = orderInfoService.doSeckill(orderMessage.getUserId(),orderMessage.getSeckillId());
            param.put("orderNo",orderNo);
            param.put("msg","下单成功,请尽快支付");
            System.out.println("监听器+++下单成功");
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE,MQConstants.ORDER_RESULT_SUCCESS_KEY,param);
        }catch (Exception e){
            param.put("code", SeckillCodeMsg.SECKILL_ERROR.getCode());
            param.put("msg", SeckillCodeMsg.SECKILL_ERROR.getMsg());
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE,MQConstants.ORDER_RESULT_FAIL_KEY,param);
            e.printStackTrace();
        }finally {
            channel.basicAck(deliveryTag,false);
        }
    }
    //如果下单失败的话的处理方法,就是如果是失败的话就通过ORDER_RESULT_EXCHANGE的交换机和ORDER_RESULT_FAIL_QUEUE队列绑定在一起,topic类型代表着是统配的意思
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.ORDER_RESULT_FAIL_QUEUE),
            exchange = @Exchange(name = MQConstants.ORDER_RESULT_EXCHANGE,type = "topic"),
            key = MQConstants.ORDER_RESULT_FAIL_KEY
    ))
    public void handleOrderFailQueue(@Payload Map<String,Object> param, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,Channel channel) throws IOException {
        try {
            Long seckillId = (Long) param.get("seckillId");
            seckillService.syncRedisStockCount(seckillId);
            System.out.println("监听器+++下单失败");
        }catch (Exception e){
            e.printStackTrace();
            //出现错误的话这里可以发送一些信息啥的告诉程序员或者谁说同步预存失败了
        }finally {
            channel.basicAck(deliveryTag,false);
        }
    }
    //下单成功的队列,这个队列不被监听,如果被监听的话就会立马有人去消费它,因为下单成功后就是支付的操作,所以我们不能够让它立马就去消费
    @Bean
    public org.springframework.amqp.core.Queue orderSuccessDelayQueue(){
        System.out.println("监听器+++下单成功等待支付");
        HashMap<String, Object> arguments = new HashMap<>();
        //死信队列的交换机
        arguments.put("x-dead-letter-exchange",MQConstants.DELAY_EXCHANGE);
        //私信队列的路由key
        arguments.put("x-dead-letter-routing-key",MQConstants.ORDER_DELAY_KEY);
        //这个的有效时间
        arguments.put("x-message-ttl",1000*60*15);
        return new org.springframework.amqp.core.Queue(MQConstants.ORDER_RESULT_SUCCESS_DELAY_QUEUE,true,false,false,arguments);
    }
    //定义orderSuccessDelayQueue和哪个交换机进行绑定,就是说我们要的就是我们通过哪个交换机去跟超时的队列去进行绑定,只是绑定,不需要有人去监听它的
    @Bean
    public Binding bindingla(org.springframework.amqp.core.Queue orderSuccessDelayQueue){
        return BindingBuilder.bind(orderSuccessDelayQueue).to(new TopicExchange(MQConstants.ORDER_RESULT_EXCHANGE))
                .with(MQConstants.ORDER_RESULT_SUCCESS_KEY);
    }
    //监听超时了没有支付的订单进行处理
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = MQConstants.ORDER_TIMEOUT_QUEUE),
        exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE,type = "topic"),
            key = MQConstants.ORDER_DELAY_KEY
    ))
    public void handleTimeOut(@Payload Map<String,Object> param,@Header(AmqpHeaders.DELIVERY_TAG)Long deliceryTag,Channel channel) throws IOException {
        try {
            Long seckillId = (Long) param.get("seckillId");
            String orderNo = (String) param.get("orderNo");
            orderInfoService.cancelOrder(seckillId,orderNo);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channel.basicAck(deliceryTag,false);
        }
    }
    //利用到广播模式然后去把本地标识取消到,然后对应的服务也会根据这个广播去把本地标识取消掉,就会达到让不同的服务器拿到本地的标识是实时的
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX,type = "fanout")
    ))
    public void handleCancleLocalSigPubSub(Long seckillId,@Header (AmqpHeaders.DELIVERY_TAG) Long deliveryTag,Channel channel) throws IOException {
        try {
            OrderInfoController.isStockOverMap.put(seckillId,false);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channel.basicAck(deliveryTag,false);
        }
    }

}
