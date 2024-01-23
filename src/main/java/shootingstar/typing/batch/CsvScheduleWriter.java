package shootingstar.typing.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.TextRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CsvScheduleWriter implements ItemWriter<TextDto> {
    private final TextRepository textRepository;

    @Override
    public void write(Chunk<? extends TextDto> chunk) {
        List<Text> scheduleList = new ArrayList<>();

        chunk.forEach(getScheduleDto -> {
            try {
                Text text = getScheduleDto.toEntity();
                scheduleList.add(text);
            } catch (Exception e) {
                throw new RuntimeException("텍스트 변환 중 오류가 발생했습니다." + e.getMessage());
            }
        });

        textRepository.saveAll(scheduleList);
    }
}
