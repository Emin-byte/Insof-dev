package io.munkush.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "generator-service")
public interface GeneratorServiceClient {

    @PostMapping("/generate")
    String generate(@RequestBody String login);
}
