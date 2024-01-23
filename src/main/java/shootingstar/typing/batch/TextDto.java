package shootingstar.typing.batch;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.Text;
import shootingstar.typing.util.TextConverter;

import java.io.IOException;

@Data
@Slf4j
@RequiredArgsConstructor
public class TextDto {
    private String lang;
    private String title;
    private String description;
    private String desText;
    private String author;

    private final TextConverter textConvertService = new TextConverter();

    public Text toEntity() throws Exception {
        CodeLanguage language = switch (this.lang) {
            case "JAVA", "\uFEFFJAVA" -> CodeLanguage.JAVA;
            case "JS", "\uFEFFJS" -> CodeLanguage.JS;
            case "CPP", "\uFEFFCPP" -> CodeLanguage.CPP;
            case "PYTHON", "\uFEFFPYTHON" -> CodeLanguage.PYTHON;
            default -> null;
        };

        String desText = textConvertService.convertJSON(textConvertService.convert(this.desText)); // 전달 받은 지문을 JSON 으로 변환
        String typingText = textConvertService.convertJSON(textConvertService.convertRemoveAnno(this.desText, language)); // 전달 받은 지문의 주석을 제거하고 JSON 으로 변환

        return new Text(language, this.title, this.description, desText, typingText, this.author);
    }
}
