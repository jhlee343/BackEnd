package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class PageListByLangDto {
    private PageInformationDto page;
    private List<FindAllTextsByLangDto> texts;

    @QueryProjection
    public PageListByLangDto(PageInformationDto pageInformationDto, List<FindAllTextsByLangDto> texts) {
        this.page = pageInformationDto;
        this.texts = texts;
    }
}
