package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.MenuRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.voting.util.ValidationUtil.checkNew;

@Service
public class MenuServiceImpl implements MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu create(Menu menu, int restaurantId) {
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id=" + restaurantId)
        );
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);

    }

    @Override
    public Menu getById(int id) {
        return menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found menu with id=" + id));
    }

    @Override
    public List<Menu> getBy(int restaurantId, LocalDate date) {
        if (date == null) return menuRepository.getByRestaurant_Id(restaurantId);
        return menuRepository.getByRestaurant_IdAndAdded(restaurantId, date);
    }

    @Override
    public Menu update(Menu menu, int restaurantId) {
        menuRepository.findById(menu.getId()).orElseThrow(() -> new NotFoundException("Not found menu with id=" + menu.getId()));
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        menu.setRestaurant(restaurant);
        return  menuRepository.save(menu);
    }

    @Override
    public void delete(int id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.getAllByOrderByIdAsc();
    }
}
