package com.nfl.national_football_league.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

public interface ClientAuthService {

    enum ResultCode {

        OK,
        WARN_AUTH,
        ERR_UNKNOWN
    }

    @Builder
    @Value
    class Result {
        @Builder.Default
        @Getter(onMethod = @__(@JsonProperty("result_code")))
        ResultCode resultCode = ResultCode.ERR_UNKNOWN;
    }

    Result verify(String jwt, String apiPath);
}
