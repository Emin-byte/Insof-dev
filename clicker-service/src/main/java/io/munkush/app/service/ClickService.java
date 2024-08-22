package io.munkush.app.service;

import io.munkush.app.service.dto.ClickRequest;
import io.munkush.app.service.dto.ClickResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClickService {

    private final ClickRepository clickRepository;
    private final ClickRequestToClickMapper clickRequestToClickMapper;
    private final ClickToClickResponseMapper clickToClickResponseMapper;


    public void save(ClickRequest request){

        var click = clickRequestToClickMapper.map(request);
        click.setCreateDate(LocalDateTime.now());

        clickRepository.save(click);
    }

    public List<ClickResponse> getAll(String username){
        var clicks = clickRepository.findAllByUsername(username);
        return clicks.stream().map(clickToClickResponseMapper::map).collect(Collectors.toList());
    }
}
