package cn.wolfcode.shop.cloud.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

@Component
public class ErrorResponseJSONFilter extends ZuulFilter {
    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        return ctx.getThrowable()!=null && !ctx.getBoolean(SEND_ERROR_FILTER_RAN,false);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Map<String,Object> map = new HashMap<>();
        if ("true".equals(ctx.get("rateLimitExceeded"))){
            map.put("code",429);
            map.put("msg","访问太频繁了,请稍后再试!");
        }else {
            map.put("code",500000);
            map.put("msg","访问人数过多,服务器繁忙");
        }
        HttpServletResponse response = ctx.getResponse();
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.setSendZuulResponse(false);
        return null;
    }
}
