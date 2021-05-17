package com.demo.supportportal.services;

public interface LoginAttemptService {

    int MAXIMUM_NO_OF_LOGIN_ATTEMPTS = 5;
    int ATTEMPT_INCREMENT = 1;

    void evictUserFromLoginAttemptCache(String username);

    void addUserToLoginAttemptCache(String username);

    boolean hasExceededMaxAttempts(String username);

}
