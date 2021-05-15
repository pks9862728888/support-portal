package com.demo.supportportal.services;

import com.demo.supportportal.exceptions.EmailExistsException;
import com.demo.supportportal.exceptions.UserNotFoundException;
import com.demo.supportportal.exceptions.UsernameExistsException;
import com.demo.supportportal.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistsException;

    List<User> getUsers();

    User findByUsername(String username);

    User findByEmail(String email);

}
