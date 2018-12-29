package org.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.RestaurantRepository;
import org.voting.util.exception.IllegalRequestDataException;
import org.voting.web.MenuRestController;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRestController.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuServiceImpl(RestaurantRepository restaurantRepository, MenuRepository menuRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    @Transactional
    public Menu create(Menu menu, int restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            menu.setRestaurant(restaurant);
            Menu created = menuRepository.saveAndFlush(menu);
            menu.getDishes().forEach(e -> {
                e.setMenu(created);
                dishRepository.save(e);
            });
            return created;
        }
        return null;
    }

    @Override
    public Menu getById(int id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu getBy(int restaurantId, LocalDate date) {
        return menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
    }

    @Override
    @Transactional
    public Menu update(Menu menu, int restaurantId) {
        Menu stored = menuRepository.findById(menu.getId()).orElse(null);
        if (stored != null && !menu.getDishes().isEmpty()) {
            if (menuRepository.delete(menu.getId()) == 1) {
                menu.setId(null);
                return create(menu, restaurantId);
            }
        }
        throw new IllegalRequestDataException("Not update menu for restaurantId=" + restaurantId);
    }

    @Override
    @Transactional
    public void delete(int id) {
        menuRepository.removeById(id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }
}
