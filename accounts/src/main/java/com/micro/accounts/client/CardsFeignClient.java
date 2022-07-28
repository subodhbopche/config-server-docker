package com.micro.accounts.client;

import com.micro.accounts.dto.Cards;
import com.micro.accounts.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
    public List<Cards> getCardDetails(Customer customer);
}
