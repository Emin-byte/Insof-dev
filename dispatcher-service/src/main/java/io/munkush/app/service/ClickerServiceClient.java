package io.munkush.app.service;

import io.munkush.app.service.dto.ClickRequest;
import io.munkush.app.service.dto.ClickResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "clicker-service")
public interface ClickerServiceClient {
    @PostMapping("/save")
    ResponseEntity<String> save(@RequestBody ClickRequest request);

    @GetMapping("/{username}")
    ResponseEntity<List<ClickResponse>> getAll(@PathVariable String username);

}
