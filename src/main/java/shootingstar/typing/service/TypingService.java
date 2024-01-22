package shootingstar.typing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.*;
import shootingstar.typing.service.dto.SaveTextDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TypingService {

    private final TextRepository textRepository;
    private boolean isComment = false;

    /**
     * P1 : 언어별 랜덤 지문 선택
     * 랜덤 id 반환
     */
    public long getRandomId(CodeLanguage lang){
        List<FindAllTextsByLangDto> langDtos = textRepository.findAllByLang(lang);
        if (langDtos.size() == 0) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        int randomIndex = (int) ((Math.random()) * langDtos.size());
        long id = langDtos.get(randomIndex).getId();
        return id;
    }

    /**
     * P2 : 언어별 페이지 리스트
     * {totalRecord, currentPage, totalPage} 및
     * {id, title, description} 조회
     */
    public String getLangPage(CodeLanguage language, int pageNumber, SortingType sortingType, String search) throws JsonProcessingException {
        PageInformationDto pageInformationDto = textRepository.findPageInformation(language, pageNumber, search);
        List<FindAllTextsByLangDto> texts = textRepository.findAllTextsByLang(language, pageNumber, sortingType, search);

        PageListByLangDto pageListByLangDto = new PageListByLangDto(pageInformationDto, texts);
        return convertJSON(pageListByLangDto);
    }

    /**
     * P3 : 설명 페이지
     * {title, description, desText} 조회
     */
    public FindDesTextByIdDto getDesText(CodeLanguage language, Long id) {
        FindDesTextByIdDto desTextDto = textRepository.findDesTextById(language, id);
        if (desTextDto == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return desTextDto;
    }

    /**
     * P4 : 타이핑 페이지
     * typingText 조회
     */
    public FindTypingTextDto getTypingText(CodeLanguage language, Long id) {
        FindTypingTextDto findTypingTextDto = textRepository.findTypingTextById(language, id);
        if (findTypingTextDto == null) {
            throw new NoSuchElementException("등록된 지문이 없습니다.");
        }
        return findTypingTextDto;
    }

    /**
     * 데이터베이스 지문 추가
     */
    @Transactional
    public Text save(SaveTextDto saveTextDto) throws JsonProcessingException {
        String desText = convertJSON(convert(saveTextDto.getText())); // 전달 받은 지문을 JSON 으로 변환
        String typingText = convertJSON(convertRemoveAnno(saveTextDto.getText(), saveTextDto.getLang())); // 전달 받은 지문의 주석을 제거하고 JSON 으로 변환

        Text text = new Text(
                saveTextDto.getLang(),
                saveTextDto.getTitle(),
                saveTextDto.getDescription(),
                desText,
                typingText,
                saveTextDto.getAuthor());

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
    public List<String> convertRemoveAnno(String text, CodeLanguage language) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            boolean previousLineEmpty = false;

            while ((line = reader.readLine()) != null) {
                String cleanedLine = "";

                if (language.equals(CodeLanguage.PYTHON)) {
                    cleanedLine = removeAnnoByPYTHON(line);
                }
                else {
                    cleanedLine = removeAnnoByLang(line);
                }

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
     * PYTHON 주석 제거
     * #, """, '''
     */
    private String removeAnnoByPYTHON(String line) {
        String cleanedLine = line.replaceAll("#.*", "");

        Pattern blockAnno = Pattern.compile("(\"\"\"|\'\'\')");
        Matcher matchBlock = blockAnno.matcher(line);

        Pattern lineBlockAnno = Pattern.compile("(\"\"\"|\'\'\').*(\"\"\"|\'\'\')");
        Matcher matchLineBlock = lineBlockAnno.matcher(line);

        // 블록 주석 시작
        if (!isComment && matchBlock.find()) {
            if (matchLineBlock.find()) {
                cleanedLine = line.replaceAll("(\"\"\"|\'\'\').*(\"\"\"|\'\'\')", "");
            }
            else {
                this.isComment = true;
                cleanedLine = line.replaceAll("(\"\"\"|\'\'\').*", "");
            }
        }
        // 블록 주석 끝
        else if (matchBlock.find()) {
            this.isComment = false;
            cleanedLine = line.replaceAll(".*(\"\"\"|\'\'\')", "");
        }
        // 블록 주석 내용
        else {
            if (this.isComment) {
                cleanedLine = "";
            }
        }

        return cleanedLine;
    }

    /**
     * JAVA, CPP, JS 주석 제거
     * //, /* *\/
     */
    public String removeAnnoByLang(String line) {
        String cleanedLine = line.replaceAll("//.*", "");

        Pattern blockAnnoBegin = Pattern.compile("/\\*.*");
        Matcher matchBlockBegin = blockAnnoBegin.matcher(line);

        Pattern blockAnnoEnd = Pattern.compile(".*\\*/");
        Matcher matchBlockEnd = blockAnnoEnd.matcher(line);

        // 블록 주석 시작
        if (matchBlockBegin.find()) {
            if (matchBlockEnd.find()) {
                cleanedLine = line.replaceAll("/\\*.*\\*/", "");
            }
            else {
                this.isComment = true;
                cleanedLine = line.replaceAll("/\\*.*", "");
            }
        }
        // 블록 주석 끝
        else if (matchBlockEnd.find()) {
            this.isComment = false;
            cleanedLine = line.replaceAll(".*\\*/", "");
        }
        // 블록 주석 내용
        else {
            if (this.isComment) {
                cleanedLine = "";
            }
        }

        return cleanedLine;
    }

    /**
     * object 를 JSON string 으로 반환
     */
    private String convertJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }
}
