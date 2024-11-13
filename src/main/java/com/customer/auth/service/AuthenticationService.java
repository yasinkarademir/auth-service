package com.customer.auth.service;

import com.customer.auth.model.Token;
import com.customer.auth.model.User;
import com.customer.auth.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final TokenService tokenService;


    public AuthenticationService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public Token registerUser(String userName, String password) {
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

    public Token loginUser(String userName, String password) {
        User user = userRepository.getUserById(userName);
        if (!Objects.equals(user.getUsername(), "")) {
            String hashedPassword = hashPassword(password);
            if (hashedPassword.equals(user.getPassword())) {
                String token = generateToken();
                String refreshToken = generateToken();
                tokenService.saveToken(token, userName);
                tokenService.saveRefreshToken(refreshToken, userName);

                return new Token(token, refreshToken, userName);
            }
        }
        return null;
    }

    public Token refreshToken(Token oldToken) {
        boolean isValid = tokenService.isRefTokenValid(oldToken.getRefreshToken());
        if (isValid) {
            String userName = oldToken.getUserName();
            String token = generateToken();
            String newRefreshToken = generateToken();
            Token newToken = new Token(token, newRefreshToken, userName);
            tokenService.saveToken(token, userName);
            tokenService.saveRefreshToken(newRefreshToken, userName);
            tokenService.invalidateToken(oldToken.getToken(), oldToken.getRefreshToken());
            return newToken;
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
