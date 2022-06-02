package com.msamaker.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "point")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPoint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", unique = true)
    private String memberId;

//    @PositiveOrZero
    @Builder.Default
    private Long balance = 0L;      // member가 가진 point 잔고

    @OneToMany(mappedBy = "memberPoint",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PointTransaction> transactionList = new ArrayList<>();

    public void addTransaction(PointTransaction transaction) {
        this.transactionList.add(transaction);
    }



}

