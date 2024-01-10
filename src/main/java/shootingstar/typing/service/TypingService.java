package shootingstar.typing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.service.dto.SaveTextDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypingService {

    private final TextRepository textRepository;

    /**
     * P1 : 언어별 랜덤 지문 선택
     * 랜덤 id 반환
     */
    public long getRandomId(CodeLanguage lang){
        List<FindAllTextsByLangDto> langDtos = textRepository.findAllByLang(lang);
        if (langDtos == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        int randomIndex = (int) ((Math.random()) * langDtos.size());
        long id = langDtos.get(randomIndex).getId();
        return id;
    }

    /**
     * P2 : 언어별 페이지 리스트
     * {id, title, description} 조회
     */
    public String getLangText(CodeLanguage lang) throws JsonProcessingException {
        List<FindAllTextsByLangDto> texts = textRepository.findAllByLang(lang);
        if (texts == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return convertJSON(texts);
    }

    /**
     * P3 : 설명 페이지
     * {title, description, desText} 조회
     */
    public FindDesTextByIdDto getDesText(Long id) {
        FindDesTextByIdDto desTextDto = textRepository.findDesTextById(id);
        if (desTextDto == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return desTextDto;
    }

    /**
     * P4 : 타이핑 페이지
     * typingText 조회
     */
    public String getTypingText(Long id) {
        Optional<Text> optionalText = textRepository.findById(id);
        if (optionalText.isEmpty()) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        Text text = optionalText.get();
        return text.getTypingText();
    }

    /**
     * 데이터베이스 지문 추가
     */
    @Transactional
    public Text save(SaveTextDto saveTextDto) throws JsonProcessingException {
        String desText = convertJSON(convert(saveTextDto.getText())); // 전달 받은 지문을 JSON 으로 변환
        String typingText = convertJSON(convertRemoveAnno(saveTextDto.getText())); // 전달 받은 지문의 주석을 제거하고 JSON 으로 변환

        Text text = new Text(
                saveTextDto.getLang(),
                saveTextDto.getTitle(),
                saveTextDto.getDescription(),
                desText,
                typingText);

        textRepository.save(text);

        return text;
    }

    /**
     * 전달 받은 텍스트를 주석 제거하지 않고 리스트로 반환
     */
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
    public List<String> convertRemoveAnno(String text) {
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
    private String convertJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
