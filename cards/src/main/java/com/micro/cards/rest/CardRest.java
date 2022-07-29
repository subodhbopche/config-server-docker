package com.micro.cards.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.cards.config.CardsServiceConfig;
import com.micro.cards.dto.Customer;
import com.micro.cards.dto.Properties;
import com.micro.cards.entity.Cards;
import com.micro.cards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;
import java.util.List;

@RestController
public class CardRest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardsServiceConfig cardsServiceConfig;

    @PostMapping("/myCards")
    public List<Cards> getCardDetails(@RequestHeader("micro_correlation_id") String correlationID, @RequestBody Customer customer) {
        System.out.println("Correlation id found for cards service :: "+ correlationID);
        List<Cards> cards = cardRepository.findByCustomerId(customer.getCustomerID());
        if (cards != null) {
            return cards;
        } else {
            return null;
        }

    }

    @GetMapping("/cards/properties")
    public String getAccountServicePropertiesDetails() throws Exception{
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(cardsServiceConfig.getMsg(), cardsServiceConfig.getBuildVersion(),
                cardsServiceConfig.getMailDetails(), cardsServiceConfig.getActiveBranches());
        return writer.writeValueAsString(properties);
    }
}
