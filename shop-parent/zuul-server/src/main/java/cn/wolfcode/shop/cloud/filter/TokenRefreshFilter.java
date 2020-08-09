package cn.wolfcode.shop.cloud.filter;

import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.web.feign.TokenFeignApi;
import cn.wolfcode.shop.cloud.util.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenRefreshFilter extends ZuulFilter {
    @Autowired
    private TokenFeignApi tokenFeignApi;
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String token = CookieUtil.getCookie(request, CookieUtil.TOKEN_COOKIE_NAME);
        if (StringUtils.isEmpty(token)){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String token = CookieUtil.getCookie(request, CookieUtil.TOKEN_COOKIE_NAME);
        Result<Boolean> result = tokenFeignApi.refresToken(token);
        if (result!=null&&!result.hasError()&&result.getData()){
            CookieUtil.addCookie(currentContext.getResponse(),CookieUtil.TOKEN_COOKIE_NAME,token,CookieUtil.TOKEN_EXPIRER_TIME);
        }
        return null;
    }
}
