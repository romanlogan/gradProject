//package com.gradproject.historyservice.interceptor;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class CustomInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////        return HandlerInterceptor.super.preHandle(request, response, handler);
//
//        String token = AuthorizationExtractor.extract(request);
//
//        // OPTIONS 요청이라면 항상 허용하도록 설정
//        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
//            setUsernameAndReturn(request, token);
//        }
//
//        if (!jwtTokenProvider.validateToken(token) || token == null) {
//            throw new AuthorizationException();
//        }
//
//        return setUsernameAndReturn(request, token);
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
//
//}
