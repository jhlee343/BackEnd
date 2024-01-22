package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FindTypingTextDto {
    private String typingText;
    private String author;

    @QueryProjection
    public FindTypingTextDto(String typingText, String author) {
        this.typingText = typingText;
        this.author = author;
    }
}
