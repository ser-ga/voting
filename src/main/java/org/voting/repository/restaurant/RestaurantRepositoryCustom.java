package org.voting.repository.restaurant;

import org.voting.model.Restaurant;

import java.time.LocalDate;

public interface RestaurantRepositoryCustom {
    Restaurant getByIdWithMenuByDate(int id, LocalDate date);
}
