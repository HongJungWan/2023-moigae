package batch.spring.metanet.component.job;

import batch.spring.metanet.component.step.DeleteEndMeetingStep;
import batch.spring.metanet.component.step.PaymentMeetingStep;
import batch.spring.metanet.component.step.UpdateMeetingStatusStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * run: --job.name=jpaBatchItemWriterJob
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaBatchItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final DeleteEndMeetingStep deleteEndMeetingStep;
    private final UpdateMeetingStatusStep updateMeetingStatusStep;
    private final PaymentMeetingStep paymentMeetingStep;

    @Bean
    public Job jpaBatchItemWriterJob() {
        return jobBuilderFactory.get("jpaBatchItemWriterJob")
                .start(updateMeetingStatusStep.updateMeetingStatusStepTwo(null))
                .next(deleteEndMeetingStep.updateEndMeetingStepOne(null))
                .next(paymentMeetingStep.updatePaymentMeetingStep(null))
                .build();
    }
}