package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import cn.wolfcode.shop.cloud.util.VerifyCodeImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {
    @Autowired
    private MyRedisTemplate myRedisTemplate;

    @RequestMapping("/getVerifyCode")
    @ResponseBody
    public void getVerifyCode(String uuid, HttpServletResponse response) throws IOException {
        String verifyCode = VerifyCodeImgUtil.generateVerifyCode();
        Integer result = VerifyCodeImgUtil.calc(verifyCode);
        myRedisTemplate.set(SeckillKeyPrefix.VERIFY_CODE_RESULT,uuid,result);
        BufferedImage codeImg = VerifyCodeImgUtil.createVerifyCodeImg(verifyCode);
        ImageIO.write(codeImg,"JPEG",response.getOutputStream());
    }
}
