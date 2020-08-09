package cn.wolfcode.shop.cloud.web;

import cn.wolfcode.shop.cloud.resolver.UserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserMethodArgumentResolver userMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userMethodArgumentResolver);
    }
    @Bean
    public UserMethodArgumentResolver userMethodArgumentResolver(){
        return new UserMethodArgumentResolver();
    }
}
