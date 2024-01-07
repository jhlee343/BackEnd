package shootingstar.typing.repository;

import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;

import java.util.List;
import java.util.Optional;

public interface TextRepositoryCustom {
    List<FindAllTextsByLangDto> findAllByLang(CodeLanguage language);

    FindDesTextByIdDto findDesTextById(Long id);
}
