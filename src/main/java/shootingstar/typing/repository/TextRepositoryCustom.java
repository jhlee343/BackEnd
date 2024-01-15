package shootingstar.typing.repository;

import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;

import java.util.List;
import java.util.Optional;

public interface TextRepositoryCustom {
    List<FindAllTextsByLangDto> findAllByLang(CodeLanguage language);

    List<FindAllTextsByLangDto> findAllByLangWithSorting(CodeLanguage language, int pageNumber, SortingType sortingType);

    long countAllByLang(CodeLanguage language);

    List<FindAllTextsByLangDto> findAllSearchWithSorting(CodeLanguage language, int pageNumber, SortingType sortingType, String target);

    long countAllSearch(CodeLanguage language, String target);

    FindDesTextByIdDto findDesTextById(Long id);
}
