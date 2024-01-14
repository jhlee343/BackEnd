package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FindDesTextByIdDto {
    private String title;
    private String description;
    private String desText;
    private String author;

    @QueryProjection
    public FindDesTextByIdDto(String title, String description, String desText, String author) {
        this.title = title;
        this.description = description;
        this.desText = desText;
        this.author = author;
    }
}
