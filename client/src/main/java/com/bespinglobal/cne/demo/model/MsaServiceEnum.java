package com.bespinglobal.cne.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MsaServiceEnum {
    Customer("Customer", 2),
    Account("Account", 9),
    History("History", 11),
    Transaction("Transaction", 13);

    private String name;
    private int index;
}
