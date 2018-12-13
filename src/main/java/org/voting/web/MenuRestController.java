package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.Dish;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.RestaurantRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
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
    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMenu(@PathVariable("restaurantId") int restaurantId, @RequestBody List<Dish> dishes) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Menu created = menuRepository.save(new Menu(LocalDate.now(), restaurant));
        dishes.forEach(e -> {
            e.setMenu(created);
            dishRepository.save(e);
        });
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/menu/{id}") //TODO убрать дублирование URI через константу
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    // TODO достать актуальное меню по Id ресторана
    @GetMapping(value = "/{restaurantId}")
    public Menu getActualMenuByRestaurantId(@PathVariable("restaurantId") int restaurantId) {
        return menuRepository.getByRestaurant_IdAndAdded(restaurantId, LocalDate.now());
    }

    // TODO обновить меню
    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMenu(@PathVariable("restaurantId") int restaurantId, @RequestBody List<Dish> dishes) {
        if (!dishes.isEmpty()) {
            Menu menu = menuRepository.getByRestaurant_IdAndAdded(restaurantId, LocalDate.now());
            if (menu != null) menuRepository.removeById(menu.getId());
        }
        return createMenu(restaurantId, dishes);
    }

}
