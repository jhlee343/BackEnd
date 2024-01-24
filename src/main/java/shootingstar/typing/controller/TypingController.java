package shootingstar.typing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.repository.dto.FindTypingTextDto;
import shootingstar.typing.service.TypingService;
import shootingstar.typing.service.dto.SaveTextDto;

import java.io.IOException;

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
     * lang 에 대한 {totalElements, number, totalPages} 및
     * lang 에 대한 content 리스트 전달
     * content : Array{id, title, description, createDate, author}
     * totalElements : 총 개수
     * totalPages : 총 페이지 수
     * number : 현재 페이지
     * @param lang 선택한 코드 언어
     * @param sortingType 선택한 정렬 방법
     * @param search 검색어
     * @param pageable (page, pageSize) ?page=으로 전달, pageSize 기본 6개로 지정
     * @return OK : Page 객체 전달
     */
    @GetMapping("/{lang}/list")
    public ResponseEntity<?> getLangListPage(@PathVariable("lang") CodeLanguage lang,
                                                    @RequestParam(value = "sortingType", required = false, defaultValue = "DATE_ASC") SortingType sortingType,
                                                    @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                    @PageableDefault(size = 6) Pageable pageable) {
        log.info("API {}", "getLangListPage");
        Page<FindAllTextsByLangDto> langPage = service.getLangPage(lang, sortingType, search, pageable);
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
    public ResponseEntity<FindTypingTextDto> getTypingText(@PathVariable("lang") CodeLanguage lang, @PathVariable("textId") Long id) {
        log.info("API {}", "getTypingText");
        FindTypingTextDto typingText = service.getTypingText(lang, id);
        return ResponseEntity.ok().body(typingText);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveText(@Validated @ModelAttribute SaveTextDto saveTextDto, BindingResult bindingResult) throws Exception {
        log.info("API {}", "saveText");
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok().body(service.save(saveTextDto));
    }
}
