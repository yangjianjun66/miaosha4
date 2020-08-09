package cn.wolfcode.shop.cloud.web.feign;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.Good;
import cn.wolfcode.shop.cloud.service.IGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class GoodFeignClient implements GoodFeignApi {
    @Autowired
    private IGoodService goodService;
    @Override
    public Result<List<Good>> queryByIds(List<Long> ids) {
        return Result.success(goodService.queryByIds(ids));
    }
}
