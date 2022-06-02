package com.msamaker.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private Member member;

//    @PositiveOrZero
    @Builder.Default
    private Long balance = 0L;  // 계좌 잔고

    @OneToMany(mappedBy = "account",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonBackReference
    private List<AccountTransaction> transactionList = new ArrayList<>();


    public void addTransaction(AccountTransaction transaction) {
        this.transactionList.add(transaction);
    }
}
