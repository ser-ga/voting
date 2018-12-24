package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.voting.model.Dish;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.RestaurantRepository;
import org.voting.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRestController.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuRestController(RestaurantRepository restaurantRepository, MenuRepository menuRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    // TODO создать меню ресторана, принимает массив блюд
    // TODO подумать как возвращать меню ресторана, возможно нужно вернуть меню ресторана за все дни
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMenu(@PathVariable("restaurantId") int restaurantId,
                                     @RequestParam(value = "date", required = false) LocalDate date,
                                     @RequestBody List<Dish> dishes) {
        dishes.forEach(ValidationUtil::checkNew);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) return ResponseEntity.notFound().build();
        Menu created = menuRepository.save(new Menu(date == null ? LocalDate.now() : date, restaurant));
        dishes.forEach(e -> {
            e.setMenu(created);
            dishRepository.save(e);
        });
        return ResponseEntity.ok().body(created);
    }

    // TODO меню по Id ресторана, по умолчанию достает за сегодня, можно указать параметер date
    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity getMenuByRestaurantId(@PathVariable("restaurantId") int restaurantId,
                                                @RequestParam(value = "date", required = false) LocalDate date) {
        Menu menu = menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
        if (menu == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(menu);
    }

    // TODO обновить меню
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMenu(@PathVariable("restaurantId") int restaurantId,
                                     @RequestParam(value = "date", required = false) LocalDate date,
                                     @RequestBody List<Dish> dishes) {
        Menu menu = menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
        if (menu != null && !dishes.isEmpty()) {
            menuRepository.removeById(menu.getId());
            return createMenu(restaurantId, date, dishes);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    //TODO возможно лучше удалять меню по menuID
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{restaurantId}")
    public void delete(@PathVariable("restaurantId") int restaurantId,
                       @RequestParam(value = "date", required = false) LocalDate date) {
        Menu menu = menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
        if (menu != null) menuRepository.removeById(menu.getId());
    }

    //TODO не забыть убрать эти методы
    @GetMapping
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }
}
