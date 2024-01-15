package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindAllTextsByLangDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate createDate;
    private String author;

    @QueryProjection
    public FindAllTextsByLangDto(Long id, String title, String description, LocalDate createDate, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.author = author;
    }
}
