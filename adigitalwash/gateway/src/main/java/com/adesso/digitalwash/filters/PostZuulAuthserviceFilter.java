//package com.adesso.digitalwash.filters;
//
/**
 * Post Zuul Filter for extracting the refresh token to a Cookie (full functionality is implemented)
 * Moved as quickfix to client side
 * for higher security move back to server
 * 
 */
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Map;
//
//import javax.servlet.http.Cookie;
//
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
//import io.micrometer.core.instrument.util.IOUtils;
//
//@Component
//public class PostZuulAuthserviceFilter extends ZuulFilter {
//    private ObjectMapper mapper = new ObjectMapper();
// 
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        try {
//            InputStream is = ctx.getResponseDataStream();
//            String responseBody = IOUtils.toString(is);
//            if (responseBody.contains("refresh_token")) {
//                Map<String, Object> responseMap = mapper.readValue(
//                  responseBody, new TypeReference<Map<String, Object>>() {});
//                String refreshToken = responseMap.get("refresh_token").toString();
//                responseMap.remove("refresh_token");
//                responseBody = mapper.writeValueAsString(responseMap);
// 
//                Cookie cookie = new Cookie("refreshToken", refreshToken);
//                cookie.setHttpOnly(true);
//                // set true for https site
//                cookie.setSecure(false);
//                cookie.setPath(ctx.getRequest().getContextPath() + "/oauth/token");
//                cookie.setMaxAge(2592000); // 30 days
//                ctx.getResponse().addCookie(cookie);
//            }
//            ctx.setResponseBody(responseBody);
//        } catch (IOException e) {
//           e.printStackTrace();
//        }
//        return null;
//    }
// 
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
// 
//    @Override
//    public int filterOrder() {
//        return 10;
//    }
// 
//    @Override
//    public String filterType() {
//        return "post";
//    }
//}