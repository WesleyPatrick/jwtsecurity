package com.wesleypatrick.jwtsecurity.mapper;

import com.wesleypatrick.jwtsecurity.controller.dto.UserResponse;
import com.wesleypatrick.jwtsecurity.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
