package com.micro.accounts.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.accounts.config.AccountServiceConfig;
import com.micro.accounts.dto.Customer;
import com.micro.accounts.dto.Properties;
import com.micro.accounts.entity.Account;
import com.micro.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceConfig accountServiceConfig;

    @PostMapping("/myAccount")
    public Account getAccountDetails(@RequestBody Customer customer){
        Account account = accountRepository.findByCustomerId(customer.getCustomerID());
        if(account != null){
            return account;
        }
        return null;
    }

    @GetMapping("/accounts/properties")
    public String getAccountServicePropertiesDetails() throws Exception{
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountServiceConfig.getMsg(), accountServiceConfig.getBuildVersion(),
                accountServiceConfig.getMailDetails(), accountServiceConfig.getActiveBranches());
        return writer.writeValueAsString(properties);
    }
}
