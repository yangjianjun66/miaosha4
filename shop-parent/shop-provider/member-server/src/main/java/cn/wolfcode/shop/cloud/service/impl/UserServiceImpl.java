package cn.wolfcode.shop.cloud.service.impl;

import cn.wolfcode.shop.cloud.common.BussinessException;
import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.mapper.UserMapper;
import cn.wolfcode.shop.cloud.msg.MemberServerCodeMsg;
import cn.wolfcode.shop.cloud.redis.MemberServerKeyPrefix;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.service.IUserService;
import cn.wolfcode.shop.cloud.util.MD5Util;
import cn.wolfcode.shop.cloud.web.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyRedisTemplate template;
    @Override
    public User find(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public String login(LoginVO loginVO) {
        if (StringUtils.isEmpty(loginVO.getUsername())||StringUtils.isEmpty(loginVO.getPassword())){
            throw new BussinessException(MemberServerCodeMsg.OP_ERROR);
        }
        User user = this.find(Long.parseLong(loginVO.getUsername()));
        if (user==null){
            throw new BussinessException(MemberServerCodeMsg.LOGIN_ERROR);
        }
        String pass = MD5Util.encode(loginVO.getPassword(),user.getSalt());
        if (!user.getPassword().equals(pass)){
            throw new BussinessException(MemberServerCodeMsg.LOGIN_ERROR);
        }
        String token = createToken(user);
        return token;
    }

    @Override
    public Boolean refreshToken(String token) {
        if (template.exists(MemberServerKeyPrefix.USER_TOKEN,token)){
            Long result = template.expire(MemberServerKeyPrefix.USER_TOKEN, token, MemberServerKeyPrefix.USER_TOKEN.getExprieSeconds());
            return result==1;
        }
        return false;
    }

    @Override
    public User getUserByToken(String token) {

        return template.get(MemberServerKeyPrefix.USER_TOKEN,token,User.class);
    }

    private String createToken(User user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        System.err.println("setToken" + token);
        template.set(MemberServerKeyPrefix.USER_TOKEN,token,user);
        return token;
    }
}
