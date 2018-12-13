package org.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Menu;

import java.time.LocalDate;

public interface MenuRepository extends JpaRepository<Menu,Integer> {

    Menu getByRestaurant_IdAndAdded(int restaurantId, LocalDate date);

    @Transactional
    @Modifying
    int removeById(int id);

}
