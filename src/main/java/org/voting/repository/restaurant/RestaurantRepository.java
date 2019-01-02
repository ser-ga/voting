package org.voting.repository.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, RestaurantRepositoryCustom {

    @Transactional
    @Modifying
    int removeById(int id);

}