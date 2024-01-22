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
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.repository.dto.FindTypingTextDtd;
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
        log.info("API {}", "randomSelect");
        long randomId = service.getRandomId(lang);
        return ResponseEntity.ok().body(randomId);
    }

    /**
     * P2 : 언어별 페이지 리스트
     * lang 에 대한 {totalRecord, currentPage, totalPage} 및
     * lang 의 {id, title, description} 리스트 전달
     * @param lang 선택한 코드 언어
     * @param page 선택한 페이지 번호
     * @param sortingType 선택한 정렬 방법
     * @param search 검색어
     * @return OK : JSON 으로 변환한 객체 리스트 전송
     */
    @GetMapping("/{lang}/list")
    public ResponseEntity<String> getLangListPage(@PathVariable("lang") CodeLanguage lang,
                                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                    @RequestParam(value = "sortingType", required = false, defaultValue = "ID_ASC") SortingType sortingType,
                                                    @RequestParam(value = "search", required = false, defaultValue = "") String search) throws JsonProcessingException {
        log.info("API {}", "getLangListPage");
        String langPage = service.getLangPage(lang, page, sortingType, search);
        return ResponseEntity.ok().body(langPage);
    }

    /**
     * P3 : 설명 페이지
     * id의 {title, description, desText} 조회
     * @FindDesTextByIdDto {title, description, desText, author} 를 가진 객체
     * @param lang 선택한 코드 언어
     * @param id 선택한 지문 id
     * @return OK : FindDesTextByIdDto 객체 전송
     */
    @GetMapping("/{lang}/description/{textId}")
    public ResponseEntity<FindDesTextByIdDto> getDescriptionText(@PathVariable("lang") CodeLanguage lang, @PathVariable("textId") Long id) {
        log.info("API {}", "getDescriptionText");
        FindDesTextByIdDto desTextDto = service.getDesText(lang, id);
        return ResponseEntity.ok().body(desTextDto);
    }

    /**
     * P4 : 타이핑 페이지
     * id의 {typingText, author} 조회
     * @FindTypingTextDtd {typingText, author} 를 가진 객체
     * @param lang 선택한 코드 언어
     * @param id 선택한 지문 id
     * @return OK : FindTypingTextDtd 객체 전송
     */
    @GetMapping("/{lang}/typingText/{textId}")
    public ResponseEntity<FindTypingTextDtd> getTypingText(@PathVariable("lang") CodeLanguage lang, @PathVariable("textId") Long id) {
        log.info("API {}", "getTypingText");
        FindTypingTextDtd typingText = service.getTypingText(lang, id);
        return ResponseEntity.ok().body(typingText);
    }

    @PostMapping("/save")
    public Object saveText(@Validated @ModelAttribute SaveTextDto saveTextDto, BindingResult bindingResult) throws JsonProcessingException {
        log.info("API {}", "saveText");
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
