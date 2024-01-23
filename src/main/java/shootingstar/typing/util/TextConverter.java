package shootingstar.typing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import shootingstar.typing.entity.CodeLanguage;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TextConverter {
    private static final Pattern lineAnno = Pattern.compile("(.*)//");
    private static final Pattern blockAnnoBegin = Pattern.compile("(.*)/\\*");
    private static final Pattern blockAnnoEnd = Pattern.compile("\\*/(.*)");
    private static final Pattern lineAnnoPython = Pattern.compile("(.*)#");
    private static final Pattern blockAnnoBeginPython = Pattern.compile("(.*?)\"\"\"|(.*?)'''");
    private static final Pattern blockAnnoEndPython = Pattern.compile("\"\"\"(.*)|'''(.*)");
    private static final Pattern blockAnnoOneLinePython = Pattern.compile("\"\"\".*?\"\"\"(.*)|'''.*?'''(.*)");


    /**
     * 전달 받은 텍스트를 주석 제거하지 않고 리스트로 반환
     */
    public List<String> convert(String text) throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            boolean previousLineEmpty = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!previousLineEmpty) {
                        line = line.strip();
                        lines.add(line);
                        previousLineEmpty = true;
                    }
                } else {
                    lines.add(line);
                    previousLineEmpty = false;
                }
            }
        } catch (Exception e) {
            throw new Exception("잘못된 형식의 텍스트입니다.");
        }

        if (lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        return lines;
    }

    /**
     * 전달 받은 텍스트를 주석 제거 후 리스트로 반환
     */
    public List<String> convertRemoveAnno(String text, CodeLanguage language) throws Exception {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            boolean previousLineEmpty = false;
            boolean isBlockComment = false;

            while ((line = reader.readLine()) != null) {
                String cleanedLine;
                lineRes res;

                if (language != CodeLanguage.PYTHON) {
                    res = removeAnnoByLang(line, isBlockComment);
                } else {
                    res = removeAnnoByLangPython(line, isBlockComment);
                }
                cleanedLine = res.line;
                isBlockComment = res.isBlockComment;

                if (cleanedLine.trim().isEmpty()) {
                    if (!previousLineEmpty) {
                        cleanedLine = cleanedLine.strip();
                        lines.add(cleanedLine);
                        previousLineEmpty = true;
                    }
                } else {
                    lines.add(cleanedLine);
                    previousLineEmpty = false;
                }
            }
        } catch (Exception e) {
            throw new Exception("잘못된 형식의 텍스트입니다.");
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
    public lineRes removeAnnoByLangPython(String line, boolean isBlockComment) {
        Matcher matchLineAnno = lineAnnoPython.matcher(line);
        Matcher matchBlockBegin = blockAnnoBeginPython.matcher(line);
        Matcher matchBlockEnd = blockAnnoEndPython.matcher(line);
        Matcher matchOneLine = blockAnnoOneLinePython.matcher(line);

        if (isBlockComment) { // 블럭 주석 내부일 경우
            if (matchBlockEnd.find()) { // 블럭 주석이 끝나는 라인
                isBlockComment = false; // 블럭 주석 끝 표시
                if (matchBlockEnd.group(1) == null) {
                    return new lineRes(matchBlockEnd.group(2).stripLeading(), isBlockComment); // 주석 후 내용, 앞 여백 제거
                } else {
                    return new lineRes(matchBlockEnd.group(1).stripLeading(), isBlockComment); // 주석 후 내용, 앞 여백 제거
                }
            } else { // 블럭 주석 내부
                return new lineRes("", isBlockComment); // 주석 제거
            }
        } else if (matchLineAnno.find()){ // 블럭 주석 내부가 아니며 라인 주석일 경우
            return new lineRes(matchLineAnno.group(1).stripTrailing(), isBlockComment); // 주석 전 내용, 뒤 여백 제거
        } else if (matchBlockBegin.find()) { // 블럭 주석 시작 부분일 경우
            if (matchOneLine.find()) {
                String beforeComment;
                String afterComment;
                String cleanLine;
                if (matchBlockBegin.group(1) == null) { // 블럭 주석이 ''' 일 경우
                    beforeComment = matchBlockBegin.group(2);
                    afterComment = matchOneLine.group(2);
                }
                else { // 블럭 주석이 """ 일 경우
                    beforeComment = matchBlockBegin.group(1);
                    afterComment = matchOneLine.group(1);
                }
                if (beforeComment.trim().isEmpty()) cleanLine = beforeComment + afterComment.stripLeading();
                else if (afterComment.trim().isEmpty()) cleanLine = beforeComment.stripTrailing();
                else cleanLine = beforeComment.stripTrailing() + " " + afterComment.stripLeading();
                return new lineRes(cleanLine, isBlockComment);
            } else {
                isBlockComment = true; // 블럭 주석 시작 표시
                if (matchBlockBegin.group(1) == null) {
                    return new lineRes(matchBlockBegin.group(2).stripTrailing(), isBlockComment); // 주석 전 부분, 뒤 공백 제거
                } else {
                    return new lineRes(matchBlockBegin.group(1).stripTrailing(), isBlockComment); // 주석 전 부분, 뒤 공백 제거
                }
            }
        } else { // 주석이 아닐 경우
            return new lineRes(line, isBlockComment);
        }
    }

    /**
     * JAVA, CPP, JS 주석 제거
     * //, /* *\/
     */
    public lineRes removeAnnoByLang(String line, boolean isBlockComment) {
        Matcher matchLineAnno = lineAnno.matcher(line);
        Matcher matchBlockBegin = blockAnnoBegin.matcher(line);
        Matcher matchBlockEnd = blockAnnoEnd.matcher(line);

        if (isBlockComment) { // 블럭 주석 내부일 경우
            if (matchBlockEnd.find()) { // 블럭 주석이 끝나는 라인
                isBlockComment = false; // 블럭 주석 끝 표시
                return new lineRes(matchBlockEnd.group(1).stripLeading(), isBlockComment); // 주석 후 내용, 앞 여백 제거
            } else { // 블럭 주석 내부
                return new lineRes("", isBlockComment); // 주석 제거
            }
        } else if (matchLineAnno.find()){ // 블럭 주석 내부가 아니며 라인 주석일 경우
            return new lineRes(matchLineAnno.group(1).stripTrailing(), isBlockComment); // 주석 전 내용, 뒤 여백 제거
        } else if (matchBlockBegin.find()) { // 블럭 주석 시작 부분일 경우
            if (matchBlockEnd.find()) { // 블럭 주석 시작과 끝이 한 라인일 경우
                // 주석 전 부분, 뒤 공백 제거 + " " + 주석 뒤 부분, 앞 공백 제거
                String beforeComment = matchBlockBegin.group(1);
                String afterComment = matchBlockEnd.group(1);
                String cleanLine;

                if (beforeComment.trim().isEmpty()) cleanLine = beforeComment + afterComment.stripLeading();
                else if (afterComment.trim().isEmpty()) cleanLine = beforeComment.stripTrailing();
                else cleanLine = beforeComment.stripTrailing() + " " + afterComment.stripLeading();
                return new lineRes(cleanLine, isBlockComment);

            } else { // 블럭 주석 시작 부분만 있을 경우
                isBlockComment = true; // 블럭 주석 시작 표시
                return new lineRes(matchBlockBegin.group(1).stripTrailing(), isBlockComment); // 주석 전 부분, 뒤 공백 제거
            }
        } else { // 주석이 아닐 경우
            return new lineRes(line, isBlockComment);
        }
    }

    /**
     * object 를 JSON string 으로 반환
     */
    public String convertJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }

    private static class lineRes {
        String line;
        boolean isBlockComment;

        private lineRes(String line, boolean isBlockComment) {
            this.line = line;
            this.isBlockComment = isBlockComment;
        }
    }
}
