package com.demo.supportportal.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    @GetMapping
    public String get() {
        return "{message: \"hello moto\"}";
    }
}
