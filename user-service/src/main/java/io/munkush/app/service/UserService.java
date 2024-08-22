package io.munkush.app.service;

import io.munkush.app.service.dto.UserCreateRequest;
import io.munkush.app.service.dto.UserResponse;
import io.munkush.app.service.mapper.UserRequestToUserMapper;
import io.munkush.app.service.mapper.UserToUserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRequestToUserMapper userRequestToUserMapper;
    private final UserToUserResponseMapper userToUserResponseMapper;
    private final UserRepository userRepository;

    public void save(UserCreateRequest request){
        if (userRepository.existsByLogin(request.login())) {
            throw new IllegalStateException(String.format("user with login: %s exists", request.login()));
        }

        var user = userRequestToUserMapper.map(request);
        user.setCreateDate(LocalDateTime.now());
        user.setCode("{noop}" + user.getCode());
        userRepository.save(user);
    }


    public boolean existsByLogin(String login){
        return userRepository.existsByLogin(login);
    }
    public List<UserResponse> getAll() {

        var users = userRepository.findAll();

        users.forEach(user -> user.setCode(removeNoopPrefix(user.getCode())));

        return users.stream()
                .map(userToUserResponseMapper::map)
                .collect(Collectors.toList());
    }


    private String removeNoopPrefix(String s) {
        if (s != null && s.startsWith("{noop}")) {
            return s.substring("{noop}".length());
        }
        return s;
    }
}
