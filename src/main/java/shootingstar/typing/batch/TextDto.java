package shootingstar.typing.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@RequiredArgsConstructor
public class TextDto {
    private String lang;
    private String title;
    private String description;
    private String desText;
    private String author;

    public Text toEntity() throws JsonProcessingException {
        CodeLanguage language = switch (this.lang) {
            case "JAVA", "\uFEFFJAVA" -> CodeLanguage.JAVA;
            case "JS", "\uFEFFJS" -> CodeLanguage.JS;
            case "CPP", "\uFEFFCPP" -> CodeLanguage.CPP;
            case "PYTHON", "\uFEFFPYTHON" -> CodeLanguage.PYTHON;
            default -> null;
        };

        String desText = convertJSON(convert(this.desText)); // 전달 받은 지문을 JSON 으로 변환
        String typingText = convertJSON(convertRemoveAnno(this.desText, language)); // 전달 받은 지문의 주석을 제거하고 JSON 으로 변환

        return new Text(language, this.title, this.description, desText, typingText, this.author);
    }

    public List<String> convert(String text) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            boolean previousLineEmpty = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!previousLineEmpty) {
                        lines.add(line);
                        previousLineEmpty = true;
                    }
                } else {
                    lines.add(line);
                    previousLineEmpty = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        return lines;
    }

    /**
     * 전달 받은 텍스트를 주석 제거 후 리스트로 반환
     */
    public List<String> convertRemoveAnno(String text, CodeLanguage language) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            boolean previousLineEmpty = false;

            while ((line = reader.readLine()) != null) {
                // 주석 제거
                String cleanedLine = line.replaceAll("//.*", "");
                if (cleanedLine.trim().isEmpty()) {
                    if (!previousLineEmpty) {
                        lines.add(cleanedLine);
                        previousLineEmpty = true;
                    }
                } else {
                    lines.add(cleanedLine);
                    previousLineEmpty = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        return lines;
    }

    /**
     * object 를 JSON string 으로 반환
     */
    public String convertJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }
}
