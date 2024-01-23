package shootingstar.typing.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.*;
import shootingstar.typing.service.dto.SaveTextDto;
import shootingstar.typing.util.TextConverter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TypingService {

    private final TextRepository textRepository;
    private final TextConverter textConverter = new TextConverter();

    /**
     * P1 : 언어별 랜덤 지문 선택
     * 랜덤 id 반환
     */
    public long getRandomId(CodeLanguage lang) {
        Long id = textRepository.randomTrackByLang(lang);
        if (id == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return id;
    }

    /**
     * P2 : 언어별 페이지 리스트
     * content : Array{id, title, description, createDate, author} 조회
     * totalElements : 총 개수
     * totalPages : 총 페이지 수
     * number : 현재 페이지
     */
    public Page<FindAllTextsByLangDto> getLangPage(CodeLanguage language, SortingType sortingType, String search, Pageable pageable) {
        return textRepository.findAllTextsByLang(language, sortingType, search, pageable);
    }

    /**
     * P3 : 설명 페이지
     * {title, description, desText} 조회
     */
    public FindDesTextByIdDto getDesText(CodeLanguage language, Long id) {
        FindDesTextByIdDto desTextDto = textRepository.findDesTextByLangAndId(language, id);
        if (desTextDto == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return desTextDto;
    }

    /**
     * P4 : 타이핑 페이지
     * typingText 조회
     */
    public FindTypingTextDto getTypingText(CodeLanguage language, Long id) {
        FindTypingTextDto findTypingTextDto = textRepository.findTypingTextByLangAndId(language, id);
        if (findTypingTextDto == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return findTypingTextDto;
    }

    /**
     * 데이터베이스 지문 추가
     */
    @Transactional
    public Text save(SaveTextDto saveTextDto) throws Exception {
        String desText = textConverter.convertJSON(textConverter.convert(saveTextDto.getText())); // 전달 받은 지문을 JSON 으로 변환
        String typingText = textConverter.convertJSON(textConverter.convertRemoveAnno(saveTextDto.getText(), saveTextDto.getLang())); // 전달 받은 지문의 주석을 제거하고 JSON 으로 변환

        Text text = new Text(
                saveTextDto.getLang(),
                saveTextDto.getTitle(),
                saveTextDto.getDescription(),
                desText,
                typingText,
                saveTextDto.getAuthor());

        textRepository.save(text);

        return text;
    }
}
