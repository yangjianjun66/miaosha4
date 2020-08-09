package cn.wolfcode.shop.cloud.service;

import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.web.vo.LoginVO;

public interface IUserService {
    User find(Long id);

    String login(LoginVO loginVO);

    Boolean refreshToken(String token);

    User getUserByToken(String token);
}
