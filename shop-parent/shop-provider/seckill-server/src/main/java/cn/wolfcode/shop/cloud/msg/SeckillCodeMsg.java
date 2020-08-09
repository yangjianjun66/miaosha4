package cn.wolfcode.shop.cloud.msg;

import cn.wolfcode.shop.cloud.common.CodeMsg;

public class SeckillCodeMsg extends CodeMsg {
    private SeckillCodeMsg(Integer code,String msg){super(code,msg);}
    public static final SeckillCodeMsg PRODUCT_SERVER_ERROR = new SeckillCodeMsg(500201,"商品服务繁忙");
    public static final SeckillCodeMsg LOGIN_TIMEOUT = new SeckillCodeMsg(500202,"登录信息过期了,请重新登录");
    public static final SeckillCodeMsg OP_ERROR = new SeckillCodeMsg(500203,"非法的操作");
    public static final SeckillCodeMsg REPEAT_SECKILL = new SeckillCodeMsg(500204,"你已经抢购过这个商品了,请勿重复抢购!");
    public static final SeckillCodeMsg SECKILL_STOCK_OVER = new SeckillCodeMsg(500205,"你来晚了,都被抢光了!");
    public static final SeckillCodeMsg SECKILL_ERROR = new SeckillCodeMsg(500206,"秒杀失败!");
    public static final SeckillCodeMsg CANCEL_ORDER_ERROR = new SeckillCodeMsg(500207,"超时取消失败了!");
    public static final SeckillCodeMsg VERIFYCODE_ERROR = new SeckillCodeMsg(500208,"验证码输入有误!");
}
