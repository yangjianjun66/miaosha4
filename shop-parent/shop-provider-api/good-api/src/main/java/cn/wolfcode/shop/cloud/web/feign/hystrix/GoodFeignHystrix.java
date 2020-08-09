package cn.wolfcode.shop.cloud.web.feign.hystrix;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.Good;
import cn.wolfcode.shop.cloud.web.feign.GoodFeignApi;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GoodFeignHystrix implements GoodFeignApi {
    @Override
    public Result<List<Good>> queryByIds(List<Long> ids) {
        System.out.println("执行了熔断GoodFeignHystrix");
        return null;
    }
}
