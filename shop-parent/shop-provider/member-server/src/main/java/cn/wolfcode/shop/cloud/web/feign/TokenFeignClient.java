package cn.wolfcode.shop.cloud.web.feign;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenFeignClient implements TokenFeignApi {
    @Autowired
    private IUserService userService;
    @Override
    public Result<Boolean> refresToken(String token) {
        return Result.success(userService.refreshToken(token));
    }
}
