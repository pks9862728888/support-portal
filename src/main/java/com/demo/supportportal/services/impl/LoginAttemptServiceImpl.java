package com.demo.supportportal.services.impl;

import com.demo.supportportal.services.LoginAttemptService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    @Value("${login.cache.expiration-time-in-minutes}")
    int cacheExpiryTimeInMinutes;

    @Value("${login.cache.max-size}")
    int maxCacheSize;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private LoadingCache<String, Integer> loginAttemptCache;

    @PostConstruct
    public void initializeCache() {
        this.loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiryTimeInMinutes, TimeUnit.MINUTES)
                .maximumSize(maxCacheSize)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(String username) {
        this.loginAttemptCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username) {
        int attempt = 0;
        try {
            attempt = loginAttemptCache.get(username) + ATTEMPT_INCREMENT;
        } catch (ExecutionException e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } finally {
            loginAttemptCache.put(username, attempt == 0 ? 1 : attempt);
        }
    }

    public boolean hasExceededMaxAttempts(String username) {
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_NO_OF_LOGIN_ATTEMPTS;
        } catch (ExecutionException e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return false;
    }

}
