package com.customer.auth.service;

import com.customer.auth.model.User;
import com.customer.auth.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final TokenService tokenService;


    public AuthenticationService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String registerUser(String userName, String password) {
        if (userRepository.getUserById(userName) == null) {
            User user = new User();
            user.setUsername(userName);
            String hashedPassword = hashPassword(password);
            user.setPassword(hashedPassword);
            userRepository.createUser(user);
            return loginUser(userName, password);
        }
        return null;
    }

    public String loginUser(String userName, String password) {
        User user = userRepository.getUserById(userName);
        if (user.getUsername() != "") {
            String hashedPassword = hashPassword(password);
            if (hashedPassword.equals(user.getPassword())) {
                String token = generateToken();
                tokenService.saveToken(token, userName);
                return token;
            }
        }
        return null;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean verifyToken(String token) {
        return tokenService.getUserNameByToken(token) != null;
    }

    private String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

}
