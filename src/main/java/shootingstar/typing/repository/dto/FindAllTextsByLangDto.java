package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FindAllTextsByLangDto {
    private Long id;
    private String title;
    private String description;

    @QueryProjection
    public FindAllTextsByLangDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
