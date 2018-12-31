package org.voting.repository.restaurant;

import org.voting.model.Restaurant;

import java.time.LocalDate;

public interface RestaurantRepositoryCustom {
    Restaurant getByIdWithMenus(int id, LocalDate date);
}
