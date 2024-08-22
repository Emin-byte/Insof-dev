package io.munkush.app.controller;

import feign.FeignException;
import io.munkush.app.service.ClickerServiceClient;
import io.munkush.app.service.GeneratorServiceClient;
import io.munkush.app.service.UserServiceClient;
import io.munkush.app.service.dto.ClickRequest;
import io.munkush.app.service.dto.UserCreateRequest;
import io.munkush.app.service.dto.UserLoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.Objects;
@Controller
@RequiredArgsConstructor
public class MainController {

    private final GeneratorServiceClient generatorServiceClient;
    private final UserServiceClient userServiceClient;
    private final ClickerServiceClient clickerServiceClient;

    @GetMapping("/")
    public String main(HttpServletRequest request, Model model) {
        var token = getAccessTokenIfValid(request);

        if (token == null) {
            return "login";
        }

        var username = getUsername(token);
        model.addAttribute("login", username);
        populateModel(model, username);
        return "table";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam("login") String login, Model model) {
        var code = generatorServiceClient.generate(login);
        UserCreateRequest userCreateRequest = new UserCreateRequest(login, code);

        try {
            userServiceClient.save(userCreateRequest).getBody();
        } catch (FeignException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return "successfull";
        }

        model.addAttribute("login", login);
        model.addAttribute("code", code);
        return "successfull";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("code") String code,
                        Model model,
                        HttpServletResponse servletResponse) {
        UserLoginRequest request = new UserLoginRequest(login, code);

        try {
            var response = userServiceClient.login(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                var accessToken = Objects.requireNonNull(response.getBody()).accessToken();
                var refreshToken = Objects.requireNonNull(response.getBody()).refreshToken();
                setupCookies(servletResponse, accessToken, refreshToken);

                model.addAttribute("login", login);
                populateModel(model, getUsername(accessToken));
                return "table";
            }
        } catch (FeignException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return "login";
        }

        return "table";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        clearCookies(response);
        model.addAttribute("msg", "Successful logout");
        return "login";
    }

    @PostMapping("/saveCoordinate")
    public String saveCoordinate(@RequestParam String x,
                                 @RequestParam String y,
                                 HttpServletRequest request,
                                 Model model) {
        var token = getAccessTokenIfValid(request);
        String username = (token != null) ? getUsername(token) : null;

        if (username != null) {
            clickerServiceClient.save(new ClickRequest(x, y, username));
        }

        populateModel(model, username);
        return "table";
    }

    private String getAccessTokenIfValid(HttpServletRequest request) {
        var cookies = request.getCookies();

        if(cookies==null){
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "access-token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(this::isTokenValid)
                .findFirst()
                .orElse(null);
    }

    private boolean isTokenValid(String token) {
        try {
            return Boolean.TRUE.equals(userServiceClient.isTokenValid(token).getBody());
        } catch (Exception e) {
            return false;
        }
    }

    private String getUsername(String token) {
        try {
            return userServiceClient.getUsername(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private void populateModel(Model model, String username) {
        if (username != null) {
            var clicks = clickerServiceClient.getAll(username).getBody();
            model.addAttribute("clicks", clicks);
        }
        model.addAttribute("codes", userServiceClient.getAll().getBody());
    }

    private void setupCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = createCookie("access-token", accessToken, 5 * 60 * 60);
        Cookie refreshCookie = createCookie("refresh-token", refreshToken, 5 * 60 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    private void clearCookies(HttpServletResponse response) {
        response.addCookie(createCookie("access-token", null, 0));
        response.addCookie(createCookie("refresh-token", null, 0));
    }
}
