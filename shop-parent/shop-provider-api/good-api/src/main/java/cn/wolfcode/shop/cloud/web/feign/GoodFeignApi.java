package cn.wolfcode.shop.cloud.web.feign;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.Good;
import cn.wolfcode.shop.cloud.web.feign.hystrix.GoodFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "good-server",fallback = GoodFeignHystrix.class)
public interface GoodFeignApi {
    @RequestMapping("/queryByIds")
    Result<List<Good>> queryByIds(@RequestParam("ids") List<Long> ids);
}
