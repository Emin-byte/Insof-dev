package io.munkush.app.service;

import io.munkush.app.service.dto.ClickRequest;
import org.springframework.stereotype.Component;

@Component
public class ClickRequestToClickMapper {

    public Click map(ClickRequest request){
        return Click.builder()
                .x(request.x())
                .y(request.y())
                .username(request.username())
                .build();
    }
}
