package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.alipay.AlipayProperties;
import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.service.ISeckillOrderSevice;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private ISeckillOrderSevice seckillOrderSevice;
    @RequestMapping("/payOrder")
    public void payOrder(String orderNo, HttpServletResponse response, HttpServletRequest request) throws IOException, AlipayApiException {
        OrderInfo orderInfo = seckillOrderSevice.find(orderNo);
        if (orderInfo == null){
            response.sendRedirect("http://localhost:/50x.html");
            return;
        }
        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        //同步的回调
        payRequest.setReturnUrl(alipayProperties.getReturnUrl());
        //异步的回调
        payRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
        //商品的订单号
        String out_trade_no = orderInfo.getOrderNo();
        //付款的金额
        String  total_amout = orderInfo.getSeckillPrice()+"";
        //订单名称
        String subject = "秒杀商品-"+orderInfo.getGoodName();
        //商品的描述
        String body ="商品详情-"+orderInfo.getGoodName();
        //把前台的参数封装成JSON 的字符串
        payRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amout +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String result = alipayClient.pageExecute(payRequest).getBody();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result);
    }

    @RequestMapping("notify_url")
    @ResponseBody
    public String notifyUrl(@RequestParam Map<String,String> param) throws AlipayApiException {
        System.out.println("异步回调=======>"+new Date());
        boolean signVerified = AlipaySignature.rsaCheckV1(param,alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), alipayProperties.getSignType());
        if (signVerified){
            String out_trade_no = param.get("out_trade_no");
            int count = seckillOrderSevice.changePayStatus(out_trade_no,OrderInfo.STATUS_ACCOUNT_PAID);
            if (count == 0){

            }
            return "success";
        }else {
            return "fail";
        }
    }

    @RequestMapping("/return_url")
    public void returnUrl(@RequestParam Map<String,String> param,HttpServletResponse response) throws AlipayApiException, IOException {
        System.out.println("同步回调=======>"+new Date());
        boolean signVerified = AlipaySignature.rsaCheckV1(param,alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(),alipayProperties.getSignType());
        if (signVerified){
            String out_trade_no = param.get("out_trade_no");
            response.sendRedirect("http://localhost/order_detail.html?orderNo="+out_trade_no);
        }else {
            response.sendRedirect("http://localhost/50x.html");
        }
    }
}
