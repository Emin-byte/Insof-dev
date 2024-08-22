package io.munkush.app.service;

import io.munkush.app.service.dto.ClickRequest;
import io.munkush.app.service.dto.ClickResponse;
import org.springframework.stereotype.Component;

@Component
public class ClickToClickResponseMapper {
    public ClickResponse map(Click click){
        return ClickResponse.builder()
                .id(click.getId())
                .x(click.getX())
                .y(click.getY())
                .username(click.getUsername())
                .createDate(click.getCreateDate())
                .build();
    }
}
