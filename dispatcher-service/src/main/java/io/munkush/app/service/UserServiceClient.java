package io.munkush.app.service;

import io.munkush.app.service.dto.UserCreateRequest;
import io.munkush.app.service.dto.UserLoginRequest;
import io.munkush.app.service.dto.UserLoginResponse;
import io.munkush.app.service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/save")
    ResponseEntity<String> save(@RequestBody UserCreateRequest request);

    @PostMapping("/login")
    ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request);

    @PostMapping("/is-token-valid")
    ResponseEntity<Boolean> isTokenValid(@RequestBody String token);

    @PostMapping("/get-username-by-token")
    ResponseEntity<String> getUsername(@RequestBody String token);

    @PostMapping
    ResponseEntity<List<UserResponse>> getAll();
}
