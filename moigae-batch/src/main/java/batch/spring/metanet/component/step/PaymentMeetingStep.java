package batch.spring.metanet.component.step;

import batch.spring.metanet.component.domain.meeting_payment.MeetingPayment;
import batch.spring.metanet.component.repository.MeetingPaymentRepository;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaymentMeetingStep {
    /**
     * 정산 기능은 - chunkSize 250개로 지정
     */
    private static final int CHUNK_SIZE = 250;

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final MeetingPaymentRepository meetingPaymentRepository;
    private final MeetingRepository meetingRepository;

    @Bean(name = "updatePaymentMeetingStep")
    public Step updatePaymentMeetingStep(@Value("{jobParameters[requestDate]}") String requestDate) {
        log.info("requestDate = {}", requestDate);
        return stepBuilderFactory.get("updatePaymentMeetingStep")
                .<MeetingPayment, MeetingPayment>chunk(CHUNK_SIZE)
                .reader(jpaCursorPaymentMeetingReader())
                .processor(compositeItemWriterProcessor())
                .writer(updatePaymentMeetingWriter())
                .build();
    }

    @Bean(name = "jpaCursorPaymentMeetingReader")
    public RepositoryItemReader<MeetingPayment> jpaCursorPaymentMeetingReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        return new RepositoryItemReaderBuilder<MeetingPayment>()
                .name("jpaCursorPaymentMeetingReader")
                .repository(meetingPaymentRepository)
                .methodName("findAll")
                .sorts(sorts)
                .build();
    }

    @Bean(name = "compositeItemWriterProcessor")
    public ItemProcessor<MeetingPayment, MeetingPayment> compositeItemWriterProcessor() {
        return meeting_payment -> {
            if ("미팅 종료".equals(meeting_payment.getMeeting().getMeetingStatus().getValue())
                    && meeting_payment.getCalculateAmount() == null
                    || meeting_payment.getCalculateAmount() == 0) {
                return meeting_payment;
            }
            return null;
        };
    }

    @Bean(name = "updatePaymentMeetingWriter")
    public ItemWriter<MeetingPayment> updatePaymentMeetingWriter() {
        return list -> {
            for (MeetingPayment meetingPayment : list) {
                //정산금액 구하는 쿼리
                Long calculateAmount = meetingPaymentRepository.calculateAmount(meetingPayment.getMeeting().getId());
                //MeetingPayment 도메인 calculateAmount 컬럼 업데이트
                meetingPaymentRepository.updateCalculateAmount(meetingPayment.getMeeting().getId(), calculateAmount);
            }
        };
    }
}