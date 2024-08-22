package io.munkush.app.service.mapper;


import io.munkush.app.service.User;
import io.munkush.app.service.dto.UserCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestToUserMapper {

    public User map(UserCreateRequest request){

        return  User.builder()
                .login(request.login())
                .code(request.code())
                .build();
    }
}
