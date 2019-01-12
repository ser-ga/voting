package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.voting.util.ValidationUtil.checkNew;
import static org.voting.util.ValidationUtil.checkNotFound;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final MenuService menuService;

    @Autowired
    public MenuServiceImpl(RestaurantRepository restaurantRepository, MenuRepository menuRepository, DishRepository dishRepository, MenuService menuService) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.menuService = menuService;
    }

    @Override
    @Transactional
    public Menu create(Menu menu, int restaurantId) {
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        checkNotFound(restaurant, "Not found restaurant with id=" + restaurantId);

        menu.setRestaurant(restaurant);
        Menu created = menuRepository.saveAndFlush(menu);
        menu.getDishes().forEach(e -> {
            e.setMenu(created);
            dishRepository.save(e);
        });
        return created;

    }

    @Override
    public Menu getById(int id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        return checkNotFound(menu, "Not found menu with id=" + id);
    }

    @Override
    public List<Menu> getBy(int restaurantId, LocalDate date) {
        if (date == null) return menuRepository.getByRestaurant_Id(restaurantId);
        return menuRepository.getByRestaurant_IdAndAdded(restaurantId, date);
    }

    @Override
    @Transactional
    public Menu update(Menu menu, int restaurantId) {
        checkNotFound(menuRepository.findById(menu.getId()).orElse(null), "Not found menu with id=" + menu.getId());
        if (!menu.getDishes().isEmpty()) {
            if (menuRepository.delete(menu.getId()) == 1) {
                menu.setId(null);
                return menuService.create(menu, restaurantId);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (menuRepository.delete(id) == 0) throw new NotFoundException("Not found menu with id=" + id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.getAllByOrderByIdAsc();
    }
}
