package shootingstar.typing.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.repository.TextRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableBatchProcessing
class TypingServiceTest {
    @Autowired
    private TextRepository textRepository;

    @Test
    public void random() throws Exception {
        //given
        CodeLanguage java = CodeLanguage.JAVA;
        CodeLanguage js = CodeLanguage.JS;
        CodeLanguage cpp = CodeLanguage.CPP;
        CodeLanguage python = CodeLanguage.PYTHON;

        //when
        Long javaId = textRepository.randomTrackByLang(java);
        Long jsId = textRepository.randomTrackByLang(js);
        Long cppId = textRepository.randomTrackByLang(cpp);
        Long pythonId = textRepository.randomTrackByLang(python);

        //then
        assertThat(textRepository.findById(javaId).get().getLang()).isEqualTo(java);
        assertThat(textRepository.findById(jsId).get().getLang()).isEqualTo(js);
        assertThat(textRepository.findById(cppId).get().getLang()).isEqualTo(cpp);
        assertThat(textRepository.findById(pythonId).get().getLang()).isEqualTo(python);
    }
}