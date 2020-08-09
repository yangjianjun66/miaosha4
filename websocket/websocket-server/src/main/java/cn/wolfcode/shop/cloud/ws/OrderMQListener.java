package cn.wolfcode.shop.cloud.ws;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OrderMQListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.ORDER_RESULT_NOTIFY_QUEUE),
            exchange = @Exchange(name = MQConstants.ORDER_RESULT_EXCHANGE,type = "topic"),
            key = MQConstants.ORDER_RESULT_NOTIFY_KEY
    ))
    public void handleOrderResultNotifyQueue(@Payload Map<String,Object> param, @Header(AmqpHeaders.DELIVERY_TAG)Long deliveryTag, Channel channel) throws IOException {
        try {
            String uuid = (String) param.get(("uuid"));
            int flag = 3;
            WebSocketServer ws = null;
            while (flag>0){
                ws = WebSocketServer.clients.get(uuid);
                System.out.println("OrderMQListener==>ws"+ws);
                if (ws!=null){
                    ws.getSession().getBasicRemote().sendText(JSON.toJSONString(param));
                    return;
                }
                flag--;
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channel.basicAck(deliveryTag,false);
        }
    }
}
