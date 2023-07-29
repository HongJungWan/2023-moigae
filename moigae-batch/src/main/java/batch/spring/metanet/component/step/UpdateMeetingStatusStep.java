package batch.spring.metanet.component.step;

import batch.spring.metanet.component.domain.meeting.Meeting;
import batch.spring.metanet.component.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateMeetingStatusStep {
    /**
     * chunkSize는 잘 조정 해보기. 보통 100개 ~ 500개 사이.
     */
    private static final int chunkSize = 100;

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final MeetingRepository meetingRepository;

    @Bean(name = "updateMeetingStatusStepTwo")
    public Step updateMeetingStatusStepTwo(@Value("{jobParameters[requestDate]}") String requestDate) {
        log.info("requestDate = {}", requestDate);
        return stepBuilderFactory.get("updateMeetingStatusStepTwo")
                .<Meeting, Meeting>chunk(chunkSize)
                .reader(jpaCursorMeetingReader())
                .processor(compositeItemWriterProcessor())
                .writer(updateMeetingStatusWriter())
                .build();
    }

    @Bean(name = "jpaCursorMeetingReaderTwo")
    public RepositoryItemReader<Meeting> jpaCursorMeetingReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        LocalDateTime oneDaysAgo = LocalDateTime.now().minusDays(1);
        return new RepositoryItemReaderBuilder<Meeting>()
                .name("jpaCursorMeetingReader")
                .repository(meetingRepository)
                .methodName("findMeetingStatusUpdateMeetings")
                .arguments(oneDaysAgo)
                .sorts(sorts)
                .build();
    }

    @Bean(name = "compositeItemWriterProcessorTwo")
    public ItemProcessor<Meeting, Meeting> compositeItemWriterProcessor() {
        return meeting -> {
            if (!"미팅 종료".equals(meeting.getMeetingStatus().getValue())) {
                return meeting;
            }
            return null;
        };
    }

    @Bean(name = "updateMeetingStatusWriterTwo")
    public ItemWriter<Meeting> updateMeetingStatusWriter() {
        LocalDateTime currentTime = LocalDateTime.now();
        return list -> {
            if (!list.isEmpty()) {
                meetingRepository.updateMeetingStatusToEND(currentTime);
            }
        };
    }
}