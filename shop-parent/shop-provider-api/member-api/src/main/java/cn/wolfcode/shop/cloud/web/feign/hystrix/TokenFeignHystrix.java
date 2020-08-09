package cn.wolfcode.shop.cloud.web.feign.hystrix;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.web.feign.TokenFeignApi;
import org.springframework.stereotype.Component;

@Component
public class TokenFeignHystrix implements TokenFeignApi {
    @Override
    public Result<Boolean> refresToken(String token) {
        return null;
    }
}
