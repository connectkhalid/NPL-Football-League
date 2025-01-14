package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.service.AuthCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service("AuthCommonService")
public class AuthCommonServiceImpl implements AuthCommonService {

    @Value("${jwt.http.request.header}")
    String tokenParamName;

    @Autowired(required = false)
    HttpServletRequest httpServletRequest;


    @Override
    public String getAccessKey() {
        String fetchedAccessKey;
        try{
            String accesskey = httpServletRequest.getHeader(tokenParamName);

            if (!accesskey.isEmpty() && accesskey.contains("Bearer")) {
                return null;
            }

            if (!StringUtils.hasText(accesskey)) {
                fetchedAccessKey = httpServletRequest.getParameter("accesskey");
            } else {
                fetchedAccessKey = accesskey;
            }

            if (!Objects.isNull(fetchedAccessKey)) {
                if (fetchedAccessKey.contains(Constants.Security.TOKEN_TYPE)) {
                    fetchedAccessKey = fetchedAccessKey.substring(Constants.Security.TOKEN_TYPE_LEN);
                }
            }
            return fetchedAccessKey;

        } catch (Throwable throwable){

        }
        return null;
    }
}
