package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FindTypingTextDtd {
    private String typingText;
    private String author;

    @QueryProjection
    public FindTypingTextDtd(String typingText, String author) {
        this.typingText = typingText;
        this.author = author;
    }
}
