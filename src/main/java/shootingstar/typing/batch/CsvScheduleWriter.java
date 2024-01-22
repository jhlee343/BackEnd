package shootingstar.typing.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.TextRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CsvScheduleWriter implements ItemWriter<TextDto> {
    private final TextRepository textRepository;

    @Override
    public void write(Chunk<? extends TextDto> chunk) throws Exception {
        List<Text> scheduleList = new ArrayList<>();

        chunk.forEach(getScheduleDto -> {
            try {
                Text text = getScheduleDto.toEntity();
                scheduleList.add(text);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        textRepository.saveAll(scheduleList);
    }
}
