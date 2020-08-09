package cn.wolfcode.shop.cloud.web.controller;

import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private IUserService userService;
    @RequestMapping("/test")
    public User test(){
        return userService.find(13088889999L);
    }
}
