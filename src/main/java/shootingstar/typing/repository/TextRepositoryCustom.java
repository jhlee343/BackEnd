package shootingstar.typing.repository;

import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.repository.dto.FindTypingTextDto;
import shootingstar.typing.repository.dto.PageInformationDto;

import java.util.List;

public interface TextRepositoryCustom {
    List<FindAllTextsByLangDto> findAllByLang(CodeLanguage language);

    List<FindAllTextsByLangDto> findAllTextsByLang(CodeLanguage language, int pageNumber, SortingType sortingType, String search);

    PageInformationDto findPageInformation(CodeLanguage language, long currentPage, String search);

    FindDesTextByIdDto findDesTextById(CodeLanguage language, Long id);

    FindTypingTextDto findTypingTextById(CodeLanguage language, Long id);
}
