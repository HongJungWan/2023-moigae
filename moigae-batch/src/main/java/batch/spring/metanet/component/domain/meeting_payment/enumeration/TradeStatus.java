package batch.spring.metanet.component.domain.meeting_payment.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStatus {
    APPLY("신청 성공"),
    SUCCESS("결제 성공"),
    FAIL("결제 실패");

    private final String value;
}