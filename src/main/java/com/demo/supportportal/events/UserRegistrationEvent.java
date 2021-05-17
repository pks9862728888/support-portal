package com.demo.supportportal.events;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    private String email;
    private String password;
    private String firstName;

    public UserRegistrationEvent(Object source, String email, String password, String firstName) {
        super(source);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }
}
