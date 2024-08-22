package io.munkush.app.service;

import io.munkush.app.service.dto.ClickRequest;
import io.munkush.app.service.dto.ClickResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClickController {

    private final ClickService clickService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid ClickRequest request,
                                       BindingResult bindingResult){

        System.out.println();
        checkErrors(bindingResult);

        clickService.save(request);


        return ResponseEntity.ok("SAVED");
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ClickResponse>> getAll(@PathVariable String username){

        return ResponseEntity.ok(clickService.getAll(username));
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
