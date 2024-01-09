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
//주소 겹치면 안되고, 컴파일러 에러난거 올리면 안됨.. generated 파일도 올리지말고