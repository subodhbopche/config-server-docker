package com.micro.accounts.client;

import com.micro.accounts.dto.Customer;
import com.micro.accounts.dto.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("loans")
public interface LoansFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myLoans", consumes = "application/json")
    public List<Loans> getLoanDetails(Customer customer);
}
