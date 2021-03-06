package com.demo.supportportal.constants;

public interface SecurityConstants {

    long EXPIRATION_TIME_IN_MILLISECONDS = 5 * 24 * 60 * 60 * 1000;  // 5 day
    String TOKEN_PREFIX = "Bearer ";
    String JWT_TOKEN_HEADER = "Jwt-Token";
    String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    String GET_ARRAYS_LLC = "Get Arrays, LLC";
    String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    String AUTHORITIES = "authorities";
    String FORBIDDEN_MESSAGE = "You need to login to access this page";
    String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    String OPTIONS_HTTP_METHOD = "OPTIONS";
    String[] PUBLIC_URLS = {
            "/api/v1/user/login",
            "/api/v1/user/register",
            "/api/v1/user/reset-password/**",
            "/api/v1/user/image/**"
    };
//    String[] PUBLIC_URLS = {
//            "**"
//    };

}
