package com.demo.supportportal.constants;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Component
public class Authority {

    public static final ArrayList<String> USER_AUTHORITIES = new ArrayList<>(Collections.singletonList("user:read"));
    public static final ArrayList<String> HR_AUTHORITIES = new ArrayList<>(Arrays.asList("user:read", "user:update"));
    public static final ArrayList<String> MANAGER_AUTHORITIES = new ArrayList<>(Arrays.asList("user:read", "user:update"));
    public static final ArrayList<String> ADMIN_AUTHORITIES = new ArrayList<>(Arrays.asList("user:read", "user:create", "user:update"));
    public static final ArrayList<String> SUPER_ADMIN_AUTHORITIES = new ArrayList<>(Arrays.asList("user:read", "user:create", "user:update", "user:delete"));

}
