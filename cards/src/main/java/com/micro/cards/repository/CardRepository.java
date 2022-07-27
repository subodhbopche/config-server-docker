package com.micro.cards.repository;

import com.micro.cards.entity.Cards;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Cards, Long> {

    List<Cards> findByCustomerId(int customerId);

}
