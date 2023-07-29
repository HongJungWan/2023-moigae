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
public class DeleteEndMeetingStep {
    /**
     * chunkSize는 잘 조정 해보기. 보통 100개 ~ 500개 사이.
     */
    private static final int chunkSize = 100;

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final MeetingRepository meetingRepository;

    @Bean(name = "updateEndMeetingStepOne")
    public Step updateEndMeetingStepOne(@Value("{jobParameters[requestDate]}") String requestDate) {
        log.info("requestDate = {}", requestDate);
        return stepBuilderFactory.get("updateEndMeetingStepOne")
                .<Meeting, Meeting>chunk(chunkSize)
                .reader(jpaCursorMeetingReader(meetingRepository))
                .processor(compositeItemWriterProcessor())
                .writer(deleteEndMeetingWriter())
                .build();
    }

    @Bean(name = "jpaCursorMeetingReaderOne")
    public RepositoryItemReader<Meeting> jpaCursorMeetingReader(MeetingRepository meetingRepository) {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return new RepositoryItemReaderBuilder<Meeting>()
                .name("jpaCursorMeetingReader")
                .repository(meetingRepository)
                .methodName("findEndDateTimeMeetings")
                .arguments(threeMonthsAgo)
                .sorts(sorts)
                .build();
    }

    /**
     * ItemProcessor는 Batch Job의 Reader로 부터 읽어들인 데이터에 대해 특정한 처리를 하는 컴포넌트.
     * 즉, 데이터에 대해 어떠한 가공 또는 변환 작업을 수행하는 중간 단계의 역할. 이 변환된 데이터는 이후 Writer에 의해 저장소에 기록.
     */
    @Bean(name = "compositeItemWriterProcessorOne")
    public ItemProcessor<Meeting, Meeting> compositeItemWriterProcessor() {
        return meeting -> {
            if (meeting.getMeetingStatus().getValue() == "미팅 종료" && meeting.getMeetingEndDateTime().isBefore(LocalDateTime.now().minusMonths(3))) {
                return meeting;
            }
            // 그렇지 않으면 null을 반환하여 writer로 전달되지 않는다.
            return null;
        };
    }

    @Bean(name = "deleteEndMeetingWriterOne")
    public ItemWriter<Meeting> deleteEndMeetingWriter() {
        return list -> {
            for (Meeting meeting : list) {
                meetingRepository.delete(meeting);
            }
        };
    }
}