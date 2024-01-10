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
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TypingController {
    private final TypingService service;

    /**
     * P1 : 언어별 랜덤 지문 선택
     * lang 의 랜덤 id 전달
     * @param lang 선택한 코드 언어
     * @return id : 랜덤 선택된 id 전송
     */
    @GetMapping("/{lang}/random")
    public ResponseEntity<Long> randomSelect(@PathVariable("lang") CodeLanguage lang){
        long randomId = service.getRandomId(lang);
        return ResponseEntity.ok().body(randomId);
    }

    /**
     * P2 : 언어별 페이지 리스트
     * lang 의 {id, title, description} 리스트 전달
     * @param lang 선택한 코드 언어
     * @return OK : JSON 으로 변환한 객체 리스트 전송
     */
    @GetMapping("/{lang}") // api 주소 추가 수정 필요
    public ResponseEntity<String> getLangTexts(@PathVariable("lang") CodeLanguage lang) throws JsonProcessingException {
        String langTexts = service.getLangText(lang);
        return ResponseEntity.ok().body(langTexts);
    }

    /**
     * P3 : 설명 페이지
     * id의 {title, description, desText} 조회
     * @FindDesTextByIdDto {title, description, desText} 를 가진 객체
     * @param id 선택한 지문 id
     * @return OK : FindDesTextByIdDto 객체 전송
     */
    @GetMapping("/description/{textId}")
    public ResponseEntity<FindDesTextByIdDto> get(@PathVariable("textId") Long id) {
        FindDesTextByIdDto desTextDto = service.getDesText(id);
        return ResponseEntity.ok().body(desTextDto);
    }

    /**
     * P4 : 타이핑 페이지
     * id의 typingText 조회
     * @param id 선택한 지문 id
     * @return OK : JSON 으로 변환한 typingText 전송
     */
    @GetMapping("/typingText/{textId}")
    public ResponseEntity<String> getTypingText(@PathVariable("textId") Long id) {
        String typingText = service.getTypingText(id);
        return ResponseEntity.ok().body(typingText);
    }

    @PostMapping("/save")
    public Object saveText(@Validated @ModelAttribute SaveTextDto saveTextDto, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        return service.save(saveTextDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
