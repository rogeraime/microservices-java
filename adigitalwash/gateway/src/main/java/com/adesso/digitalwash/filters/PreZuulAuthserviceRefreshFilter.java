//package com.adesso.digitalwash.filters;
//
/**
 * Pre Zuul Filter for extracting the refresh token from Cookie to Request URI Parameters (Adding the Parameters is not working)
 * Moved as quickfix to client side
 * for higher security move back to server
 * 
 */
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
//@Component
//public class PreZuulAuthserviceRefreshFilter extends ZuulFilter {
//	Logger logger = LoggerFactory.getLogger(PreZuulAuthserviceRefreshFilter.class);
//
//	@Override
//	public Object run() {
//		RequestContext ctx = RequestContext.getCurrentContext();
//		HttpServletRequest req = ctx.getRequest();
//		if (req.getRequestURI().contains("/oauth/token")) {
//			String refreshToken = extractRefreshToken(req);
//			if (refreshToken != null) {
//				CustomHttpServletRequest editedReq = new CustomHttpServletRequest(req);
//				editedReq.addParameter("grant_type", "refresh_token");
//				editedReq.addParameter("refresh_token", refreshToken);
//				ctx.setRequest(editedReq);
//			}
//		}
//		return ctx;
//	}
//
//	private String extractRefreshToken(HttpServletRequest req) {
//		Cookie[] cookies = req.getCookies();
//		if (cookies != null) {
//			for (int i = 0; i < cookies.length; i++) {
//				if (cookies[i].getName().equalsIgnoreCase("refreshToken")) {
//					return cookies[i].getValue();
//				}
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public boolean shouldFilter() {
//		return true;
//	}
//
//	@Override
//	public int filterOrder() {
//		return -3;
//	}
//
//	@Override
//	public String filterType() {
//		return "pre";
//	}
//}