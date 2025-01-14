package com.nfl.national_football_league.interceptor;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.service.ClientAuthService;
import com.nfl.national_football_league.util.HttpServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientAuthInterceptor implements HandlerInterceptor {
    @Autowired private ClientAuthService clientAuthService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ClientAuthService.Result result = clientAuthService.verify(
                request.getHeader(Constants.HttpHeaders.HEADER_KEY_CLIENT_AUTH),
                request.getRequestURI());
        if (!result.getResultCode().equals(ClientAuthService.ResultCode.OK)) {
            HttpServletUtil.doResponseJson(response, result);
            return false;
        }
        return true;
    }
}
