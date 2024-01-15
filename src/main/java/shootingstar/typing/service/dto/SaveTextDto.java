package shootingstar.typing.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import shootingstar.typing.entity.CodeLanguage;

@Data
public class SaveTextDto {
    @NotNull(message = "코드 언어 필수")
    private CodeLanguage lang;
    @NotBlank(message = "제목 필수")
    private String title;
    @NotBlank(message = "설명 필수")
    private String description;
    @NotBlank(message = "본문 필수")
    private String text;
    @NotBlank(message = "작성자 필수")
    private String author;
}
