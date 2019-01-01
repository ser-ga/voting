package org.voting.service;

import org.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuService {

    Menu create(Menu menu, int restaurantId);

    Menu getById(int id);

    List<Menu> getBy(int restaurantId, LocalDate date);

    Menu update(Menu menu, int restaurantId);

    void delete(int id);

    List<Menu> getAll();
}
