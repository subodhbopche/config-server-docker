package com.micro.accounts.dto;

import com.micro.accounts.entity.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class CustomerDetails {
    private Account account;
    private List<Cards> cards;
    private List<Loans> loans;

}
