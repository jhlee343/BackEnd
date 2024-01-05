package shootingstar.typing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.service.TypingService;
import shootingstar.typing.service.dto.SaveTextDto;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TypingController {
    private final TypingService service;

    @PostMapping("/save")
    public Object receiveInput(@Validated @ModelAttribute SaveTextDto saveTextDto, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        return service.save(saveTextDto);
    }

    @GetMapping("/typingText/{id}")
    public String get(@PathVariable("id") Long id) {
        return service.getTypingText(id);
    }

    @GetMapping("/{lang}")
    public String getLangTexts(@PathVariable("lang") CodeLanguage lang) throws JsonProcessingException {
        return service.getLangText(lang);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
