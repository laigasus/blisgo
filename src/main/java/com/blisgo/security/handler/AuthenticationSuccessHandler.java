package com.blisgo.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationSuccessHandler.class);
    private final RequestCache requestCache = new CookieRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        //기본 url 설정, savedRequest가 null일 경우 설정한 페이지로 보내기 위함이다.
        setDefaultTargetUrl("/");

        // 사용자가 인증을 시도하기 이전에 접근을 시도했던 자원이 없을경우 savedRequest는 null로 반환된다.
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }

}