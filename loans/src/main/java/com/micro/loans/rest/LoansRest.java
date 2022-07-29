package com.micro.loans.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.loans.config.LoansServiceConfig;
import com.micro.loans.dto.Customer;
import com.micro.loans.dto.Properties;
import com.micro.loans.entity.Loans;
import com.micro.loans.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;
import java.util.List;

@RestController
public class LoansRest {

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private LoansServiceConfig loansServiceConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestHeader("micro_correlation_id") String correlationID, @RequestBody Customer customer) {
        System.out.println("Correlation id found for loans service ::  "+ correlationID);
        System.out.println("this sop used to check retry count for Loans Service !!!!");
        List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerID());
        if (loans != null) {
            return loans;
        } else {
            return null;
        }

    }

    @GetMapping("/loans/properties")
    public String getAccountServicePropertiesDetails() throws Exception{
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(loansServiceConfig.getMsg(), loansServiceConfig.getBuildVersion(),
                loansServiceConfig.getMailDetails(), loansServiceConfig.getActiveBranches());
        return writer.writeValueAsString(properties);
    }
}
