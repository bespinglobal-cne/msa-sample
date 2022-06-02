package com.msamaker.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AccountTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    @Builder.Default
    private Long amount = 0L;   // 거래 금액

    private String notes;       // 거래 메모

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime dateTime; // 거래 시간

//    @PositiveOrZero
    @Builder.Default
    private Long balance = 0L;  // 계좌 잔고

    @PrePersist
    public void initDateTime() {
        this.dateTime = LocalDateTime.now();
    }
}
