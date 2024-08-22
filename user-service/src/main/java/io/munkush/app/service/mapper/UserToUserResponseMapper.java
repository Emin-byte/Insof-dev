package io.munkush.app.service.mapper;

import io.munkush.app.service.User;
import io.munkush.app.service.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseMapper {


    public UserResponse map(User user){

        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .code(user.getCode())
                .createDate(user.getCreateDate())
                .build();
    }
}
