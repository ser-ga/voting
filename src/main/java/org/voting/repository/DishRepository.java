package org.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voting.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {}
