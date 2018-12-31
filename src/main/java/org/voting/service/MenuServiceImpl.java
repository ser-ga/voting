package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.exception.IllegalRequestDataException;
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

    @Autowired
    public MenuServiceImpl(RestaurantRepository restaurantRepository, MenuRepository menuRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
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
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu getBy(int restaurantId, LocalDate date) {
        return menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
    }

    @Override
    @Transactional
    public Menu update(Menu menu, int restaurantId) {
        if (menu.getId() == null) throw new IllegalRequestDataException("Menu must be with id");

        Menu stored = menuRepository.findById(menu.getId()).orElse(null);
        checkNotFound(stored, "Not found menu with id=" + menu.getId());
        if (stored != null && !menu.getDishes().isEmpty()) {
            if (menuRepository.delete(menu.getId()) == 1) { //TODO может не нужно данное условие
                menu.setId(null);
                return create(menu, restaurantId);
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
        return menuRepository.findAll();
    }
}
