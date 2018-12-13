package org.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE r.id=?1 AND m.added=?2")
    Restaurant getByIdWithMenuOfDay(int id, LocalDate day);

    //TODO возможно это тестовый методб не забыть убрать
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE m.added=?1")
    List<Restaurant> getAllWithMenuOfDay(LocalDate day);

    @Transactional
    @Modifying
    int removeById(int id);
}


