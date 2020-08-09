package cn.wolfcode.shop.cloud.resolver;

import cn.wolfcode.shop.cloud.domain.User;
import cn.wolfcode.shop.cloud.redis.MemberServerKeyPrefix;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.RedisValue;
import cn.wolfcode.shop.cloud.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class UserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private MyRedisTemplate template;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == User.class && methodParameter.getParameterAnnotation(RedisValue.class)!=null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        System.err.println("++++++++++++++>>>自定义参数解析器已运行");
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookieUtil.getCookie(request, CookieUtil.TOKEN_COOKIE_NAME);
        System.out.println("token==>" + token);
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = template.get(MemberServerKeyPrefix.USER_TOKEN, token, User.class);
        return user;
    }
}
