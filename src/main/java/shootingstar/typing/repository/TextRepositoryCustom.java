package shootingstar.typing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.repository.dto.FindTypingTextDto;

import java.util.List;

public interface TextRepositoryCustom {
    List<FindAllTextsByLangDto> findAllByLang(CodeLanguage language);
    FindDesTextByIdDto findDesTextByLangAndId(CodeLanguage language, Long id);
    FindTypingTextDto findTypingTextByLangAndId(CodeLanguage language, Long id);
    Page<FindAllTextsByLangDto> findAllTextsByLang(CodeLanguage language, SortingType sortingType, String search, Pageable pageable);
}
