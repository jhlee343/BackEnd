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
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.service.TypingService;
import shootingstar.typing.service.dto.SaveTextDto;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TypingController {
    private final TypingService service;

    @PostMapping("/save")
    public Object saveText(@Validated @ModelAttribute SaveTextDto saveTextDto, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        return service.save(saveTextDto);
    }

    /**
     * id의 타이핑 지문 조회
     * @param id 선택한 지문 id
     * @return OK : JSON 으로 변환한 typingText 전송
     */
    @GetMapping("/typingText/{textId}")
    public ResponseEntity<String> getTypingText(@PathVariable("textId") Long id) {
        String typingText = service.getTypingText(id);
        return ResponseEntity.ok().body(typingText);
    }

    /**
     * id의 {제목, 설명, 주석 코드} 조회
     * @FindDesTextByIdDto {제목, 설명, 주석 코드} 를 가진 객체
     * @param id 선택한 지문 id
     * @return OK : FindDesTextByIdDto 객체 전송
     */
    @GetMapping("/desText/{textId}")
    public ResponseEntity<FindDesTextByIdDto> get(@PathVariable("textId") Long id) throws JsonProcessingException {
        FindDesTextByIdDto desTextDto = service.getDesText(id);
        return ResponseEntity.ok().body(desTextDto);
    }

    /**
     * lang 의 {아이디, 제목, 설명} 리스트 전달
     * @param lang 선택한 코드 언어
     * @return OK : JSON 으로 변환한 객체 리스트 전송
     */
    @GetMapping("/{lang}")
    public ResponseEntity<String> getLangTexts(@PathVariable("lang") CodeLanguage lang) throws JsonProcessingException {
        String langTexts = service.getLangText(lang);
        return ResponseEntity.ok().body(langTexts);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
