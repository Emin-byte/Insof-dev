package io.munkush.app.service;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GeneratorService {


    public String generate(String login){
        Random random = new Random(login.hashCode());
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }

}
