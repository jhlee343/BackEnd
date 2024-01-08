package shootingstar.typing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shootingstar.typing.entity.CodeLanguage;

@RestController
@RequiredArgsConstructor
public class SelectController {

    @GetMapping("/{langague}")
    public String selectLang(@PathVariable("langague") CodeLanguage lang){
        return "{lang}/texts";

    }
}
