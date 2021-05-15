package com.demo.supportportal.enumerations;

import com.demo.supportportal.constants.Authority;

import java.util.ArrayList;

public enum Role {
    ROLE_USER(Authority.USER_AUTHORITIES),
    ROLE_HR(Authority.HR_AUTHORITIES),
    ROLE_MANAGER(Authority.MANAGER_AUTHORITIES),
    ROLE_ADMIN(Authority.ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(Authority.SUPER_ADMIN_AUTHORITIES);

    private ArrayList<String> authorities;

    Role(ArrayList<String> authorities) {
        this.authorities = authorities;
    }

    public ArrayList<String> getAuthorities() {
        return this.authorities;
    }
}
