package com.huatuo.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.huatuo.apigateway.exception.RateLimiterException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流过滤器
 */
@Component
public class RateLimiterFilter extends ZuulFilter {

    /**
     * 水桶限流
     */
    public static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        //尝试从水桶里面拿令牌
        if (!RATE_LIMITER.tryAcquire()) {
            throw new RateLimiterException();
        }
        return null;
    }
}
