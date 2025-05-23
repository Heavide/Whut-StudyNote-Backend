package com.example.studynotebackend.service;

import com.example.studynotebackend.domain.UserAccount;
import com.example.studynotebackend.dto.LoginRequest;
import com.example.studynotebackend.dto.LoginResponse;
import com.example.studynotebackend.dto.RegisterRequest;
import com.example.studynotebackend.mapper.UserMapper;
import com.example.studynotebackend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        // 检查用户名或邮箱是否已存在
        UserAccount existing = userMapper.findByUsernameOrEmail(req.getUsername(), req.getEmail());
        if (existing != null) {
            throw new RuntimeException("用户名或邮箱已存在");
        }
        // 加密密码
        UserAccount user = new UserAccount();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        userMapper.insert(user);
    }

    public LoginResponse login(LoginRequest req) {
        // 查用户
        UserAccount user = userMapper.findByUsernameOrEmail(req.getUsernameOrEmail(), req.getUsernameOrEmail());
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("用户名/邮箱或密码错误");
        }
        // 生成 JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(token);
    }

    /**
     * 根据用户名查用户，用于 /api/auth/me
     */
    public UserAccount findByUsername(String username) {
        UserAccount user = userMapper.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
}
