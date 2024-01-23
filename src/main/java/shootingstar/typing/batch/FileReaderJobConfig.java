package shootingstar.typing.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FileReaderJobConfig {
    private final PlatformTransactionManager transactionManager;
    private final CsvScheduleWriter csvScheduleWriter;
    private final CsvReader csvReader;
    private static final int chunkSize = 1000;
    @Bean
    public Job csvScheduleJob(JobRepository jobRepository) {
        return new JobBuilder("csvScheduleJob", jobRepository)
                .start(csvScheduleReaderStep(jobRepository))
                .build();
    }

    @Bean
    public Step csvScheduleReaderStep(JobRepository jobRepository){
        return new StepBuilder("csvScheduleReaderStep", jobRepository)
                .<TextDto, TextDto>chunk(chunkSize, transactionManager)
                .reader(csvReader.csvScheduleReader())
                .writer(csvScheduleWriter)
                .allowStartIfComplete(true) // 서버 시작시 Step 을 다시 실행
                .build();
    }
}
