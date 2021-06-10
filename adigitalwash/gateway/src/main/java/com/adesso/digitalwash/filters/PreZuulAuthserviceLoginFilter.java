package com.adesso.digitalwash.filters;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreZuulAuthserviceLoginFilter extends ZuulFilter {
    Logger logger = LoggerFactory.getLogger(PreZuulAuthserviceLoginFilter.class);
	
	@Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getRequestURI().contains("/oauth/token")) {
            byte[] encoded;
            try {
                encoded = Base64.encode("laundryclient:secret".getBytes("UTF-8"));
                ctx.addZuulRequestHeader("authorization", "Basic " + new String(encoded));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
 
    @Override
    public boolean shouldFilter() {
        return true;
    }
 
    @Override
    public int filterOrder() {
        return -2;
    }
 
    @Override
    public String filterType() {
        return "pre";
    }
}