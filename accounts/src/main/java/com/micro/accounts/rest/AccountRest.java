package com.micro.accounts.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.accounts.client.CardsFeignClient;
import com.micro.accounts.client.LoansFeignClient;
import com.micro.accounts.config.AccountServiceConfig;
import com.micro.accounts.dto.*;
import com.micro.accounts.entity.Account;
import com.micro.accounts.repository.AccountRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;
import java.util.List;

@RestController
public class AccountRest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceConfig accountServiceConfig;

    @Autowired
    private CardsFeignClient cardsFeignClient;

    @Autowired
    private LoansFeignClient loansFeignClient;

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
/*
* we can not call both at the same time circuitBreaker and Retry if we do then circuitBreaker is called
* if you want to try to call the service multiple time for network issue then  you should go through @Retry Annotation
* */
    @PostMapping("/myCustomerDetails")
    /*@CircuitBreaker(name = "detailsForCustomerDetailsSupportApp", fallbackMethod = "myAccountDetailsHandler")
    @CircuitBreaker(name = "detailsForCustomerDetailsSupportApp")*/
    @Retry(name = "retryForCustomerDetails", fallbackMethod = "myAccountDetailsRetryHandler")
    public CustomerDetails myCustomerDetails(@RequestHeader("micro_correlation_id") String correlationID, @RequestBody Customer customer){
        System.out.println("Correlation id found for accounts service ::  "+ correlationID);
        Account account = accountRepository.findByCustomerId(customer.getCustomerID());
        List<Loans> loans = loansFeignClient.getLoanDetails(correlationID,customer);
        List<Cards> cards = cardsFeignClient.getCardDetails(correlationID, customer);
        System.out.println("Accounts ");
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setCards(cards);
        customerDetails.setLoans(loans);

        return customerDetails;
    }

    private CustomerDetails myAccountDetailsHandler(@RequestHeader("micro_correlation_id") String correlationID, Customer customer, Throwable throwable){
        System.out.println("Correlation id found for myAccountDetailsHandler "+ correlationID);
        Account account = accountRepository.findByCustomerId(customer.getCustomerID());
        List<Loans> loans = loansFeignClient.getLoanDetails(correlationID, customer);
        System.out.println("Accounts handler "+ throwable.getMessage());
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setLoans(loans);

        return customerDetails;


    }private CustomerDetails myAccountDetailsRetryHandler(@RequestHeader("micro_correlation_id")  String correlationID,Customer customer, Throwable throwable){
        System.out.println("Correlation id found for myAccountDetailsRetryHandler "+ correlationID);
        Account account = accountRepository.findByCustomerId(customer.getCustomerID());
        List<Loans> loans = loansFeignClient.getLoanDetails(correlationID, customer);
        System.out.println("myAccountDetailsRetryHandler Accounts handler "+ throwable.getMessage());
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setLoans(loans);

        return customerDetails;
    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to subodh";
    }

    private String sayHelloFallback(Throwable t) {
        return "Hi, Welcome to subodh";
    }
}
