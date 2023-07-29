package batch.spring.metanet.component.domain.meeting_payment;

import batch.spring.metanet.component.domain.meeting.Meeting;
import batch.spring.metanet.component.domain.meeting_payment.enumeration.RefundStatus;
import batch.spring.metanet.component.domain.meeting_payment.enumeration.TradeStatus;
import batch.spring.metanet.component.domain.user.User;
import batch.spring.metanet.core.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "meeting_payment")
@NoArgsConstructor
@Getter
public class MeetingPayment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    //결제 (성공, 실패) 여부
    @Column(name = "trade_status")
    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    //주문 번호
    @Column(name = "meeting_order_id")
    private String meetingOrderId;

    //결제된 금액
    @Column(name = "paid_amount")
    private Long paidAmount;

    //정산된 금액
    @Column(name = "calculate_amount")
    private Long calculateAmount;

    //환불 체크
    @Column(name = "refund_status")
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @Builder
    public MeetingPayment(User user, Meeting meeting, TradeStatus tradeStatus, String meetingOrderId,
                          Long paidAmount, Long calculateAmount, RefundStatus refundStatus) {
        this.user = user;
        this.meeting = meeting;
        this.tradeStatus = tradeStatus;
        this.meetingOrderId = meetingOrderId;
        this.paidAmount = paidAmount;
        this.calculateAmount = calculateAmount;
        this.refundStatus = refundStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingPayment that = (MeetingPayment) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(meeting, that.meeting) && tradeStatus == that.tradeStatus && Objects.equals(meetingOrderId, that.meetingOrderId) && Objects.equals(paidAmount, that.paidAmount) && Objects.equals(calculateAmount, that.calculateAmount) && refundStatus == that.refundStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, meeting, tradeStatus, meetingOrderId, paidAmount, calculateAmount, refundStatus);
    }
}