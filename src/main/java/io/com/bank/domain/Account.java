package io.com.bank.domain;

import io.com.bank.exception.CustomApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "account_tb")
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 8)
    private Long number;

    @Column(nullable = false, length = 4)
    private Long password;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 계좌주인 확인
    public void checkOwner(Long memberId) {
        if (member.getId() != memberId) {
            throw new CustomApiException("계좌 소유자가 아닙니다");
        }
    }

    // 입금
    public void deposit(Long amount) {
        balance = balance + amount;
    }

    // 비밀번호 확인
    public void checkPassword(Long password) {
        if (this.password != password) {
            throw new CustomApiException("비밀번호가 틀렸습니다");
        }
    }

    // 계좌 금액 확인
    public void checkBalance(Long amount) {
        if (balance < amount) {
            throw new CustomApiException("계좌 잔액이 부족합니다");
        }
    }

    // 출금
    public void withdraw(Long amount) {
        balance = balance - amount;
    }
}
