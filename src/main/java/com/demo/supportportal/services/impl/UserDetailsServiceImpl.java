package com.demo.supportportal.services.impl;

import com.demo.supportportal.exceptions.EmailExistsException;
import com.demo.supportportal.exceptions.UserNotFoundException;
import com.demo.supportportal.exceptions.UsernameExistsException;
import com.demo.supportportal.models.User;
import com.demo.supportportal.models.UserPrincipal;
import com.demo.supportportal.repositories.UserRepository;
import com.demo.supportportal.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.demo.supportportal.constants.UserImplementationConstant.*;
import static com.demo.supportportal.enumerations.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserDetailsServiceImpl implements UserService {

    @Value("${default.user.image.path}")
    private String DEFAULT_USER_IMAGE_PATH;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOGGER.error(NO_USER_FOUND_WITH_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_WITH_USERNAME + username);
        } else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.saveAndFlush(user);
            return new UserPrincipal(user);
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        getUserAfterValidatingUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(firstName));
        String password = generatePassword();
        user.setPassword(this.passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
        LOGGER.info("Password: " + password);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String getTemporaryProfileImageUrl(String firstName) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH)
                .toUriString();
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10, 15);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private Optional<User> getUserAfterValidatingUsernameAndEmail(String currentUsername, String newUsername, String email) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_WITH_USERNAME + currentUsername);
            }

            if (StringUtils.isNotBlank(newUsername)) {
                User userByNewUsername = findByUsername(newUsername);
                if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                    throw new UsernameExistsException(USERNAME_EXISTS + newUsername);
                }
            }

            if (StringUtils.isNotBlank(email)) {
                User userByNewEmail = findByEmail(email);
                if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                    throw new EmailExistsException(EMAIL_ALREADY_REGISTERED + email);
                }
            }

            return Optional.of(currentUser);
        } else {
            User userByUsername = findByUsername(newUsername);
            if (userByUsername != null) {
                throw new UsernameExistsException(USERNAME_EXISTS + newUsername);
            }

            User userByEmail = findByEmail(email);
            if (userByEmail != null) {
                throw new EmailExistsException(EMAIL_ALREADY_REGISTERED);
            }

            return Optional.empty();
        }
    }
}
