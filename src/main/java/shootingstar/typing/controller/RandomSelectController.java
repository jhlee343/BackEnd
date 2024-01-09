package shootingstar.typing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.service.RandomService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RandomSelectController {
    private final RandomService service;

    @GetMapping("/api/{lang}/random")
    public long randomSelect(@PathVariable("lang") CodeLanguage lang){
        //textId 값 반환 //
        return service.getLangdomCount(lang);
    }
}
