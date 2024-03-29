package batch.spring.metanet.component.domain.meeting_payment.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundStatus {
    REQUESTED("환불 요청"),
    PROCESSING("환불 처리 중"),
    COMPLETED("환불 완료"),
    DENIED("환불 거부"),
    NONE("없음");

    private final String value;
}