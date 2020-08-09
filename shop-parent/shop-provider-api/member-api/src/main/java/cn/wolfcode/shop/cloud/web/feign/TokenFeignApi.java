package cn.wolfcode.shop.cloud.web.feign;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.web.feign.hystrix.TokenFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-server",fallback = TokenFeignHystrix.class)
public interface TokenFeignApi {
    @RequestMapping("/refresToken")
    public Result<Boolean> refresToken(@RequestParam("token") String token);
}
