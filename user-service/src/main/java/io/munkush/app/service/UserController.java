package io.munkush.app.service;

import io.munkush.app.security.JwtGenerator;
import io.munkush.app.security.UserDetailsServiceImpl;
import io.munkush.app.service.dto.UserCreateRequest;
import io.munkush.app.service.dto.UserLoginRequest;
import io.munkush.app.service.dto.UserLoginResponse;
import io.munkush.app.service.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtGenerator jwtGenerator;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid UserCreateRequest request,
                                       BindingResult bindingResult){


        checkErrors(bindingResult);

        userService.save(request);

        return ResponseEntity.ok(jwtGenerator.createAccess(request.login()));

    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request,
                                                   BindingResult bindingResult){
        checkErrors(bindingResult);

        var login = request.login();

        if(!userService.existsByLogin(login)){
            throw new IllegalStateException(String.format("user with login: %s not exists", login));
        }

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(login, request.code(), null);
        var authenticate = authenticationManager.authenticate(authenticationToken);

        if(authenticate.isAuthenticated()){

            UserLoginResponse response = new UserLoginResponse(
                    jwtGenerator.createAccess(login),
                    jwtGenerator.createRefresh(login)
            );

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    @PostMapping("/is-token-valid")
    public ResponseEntity<Boolean> checkToken(@RequestBody String token){


        if(!jwtGenerator.isTokenExpired(token)){


            var username = jwtGenerator.getUsername(token);

            if(userDetailsService.loadUserByUsername(username) != null){
                return ResponseEntity.ok(true);
            }

        }

        return ResponseEntity
                .badRequest()
                .body(false);

    }

    @PostMapping
    public ResponseEntity<List<UserResponse>> getAll(){

        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/get-username-by-token")
    public ResponseEntity<String> getUsername(@RequestBody String token){

        var username = jwtGenerator.getUsername(token);
        return ResponseEntity.ok(username);
    }


    private void checkErrors(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            String errorsString = String.join(", ", errors);

            throw new IllegalStateException(errorsString);
        }

    }
}
