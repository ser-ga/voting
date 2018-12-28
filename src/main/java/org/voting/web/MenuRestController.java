package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.voting.model.Menu;
import org.voting.model.Restaurant;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.repository.RestaurantRepository;
import org.voting.to.MenuTo;
import org.voting.util.ValidationUtil;
import org.voting.util.exception.IllegalRequestDataException;

import java.time.LocalDate;
import java.util.List;

import static org.voting.util.ValidationUtil.checkNew;

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

    //TODO попробовать сделать через MenuTo
    // TODO создать меню ресторана, принимает массив блюд
    // TODO подумать как возвращать меню ресторана, возможно нужно вернуть меню ресторана за все дни
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMenu(@Validated @RequestBody MenuTo menuTo) {
        checkNew(menuTo);
        menuTo.getDishes().forEach(ValidationUtil::checkNew);
        Restaurant restaurant = restaurantRepository.findById(menuTo.getRestaurantId()).orElse(null);
        if (restaurant == null) return ResponseEntity.notFound().build();
        Menu created = menuRepository.save(new Menu(menuTo.getDate() == null ? LocalDate.now() : menuTo.getDate(), restaurant));
        menuTo.getDishes().forEach(e -> {
            e.setMenu(created);
            dishRepository.save(e);
        });
        return ResponseEntity.ok().body(menuRepository.findById(created.getId()));
    }

    // TODO меню по Id
    @GetMapping(value = "/{id}")
    public ResponseEntity getMenuById(@PathVariable("id") int id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(menu);
    }

    // TODO меню по Id ресторана, по умолчанию достает за сегодня, можно указать параметер date
    @GetMapping(value = "/by")
    public ResponseEntity getMenuByRestaurantId(@RequestParam("restaurantId") int restaurantId,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "date", required = false) LocalDate date) {
        Menu menu = menuRepository.getByRestaurant_IdAndAdded(restaurantId, date == null ? LocalDate.now() : date);
        if (menu == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(menu);
    }

    // TODO обновить меню
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMenu(@Validated @RequestBody MenuTo menuTo) {
        if(menuTo.getId() == null) throw new IllegalRequestDataException("Menu must be with id");
        Menu menu = menuRepository.findById(menuTo.getId()).orElse(null);
        if (menu != null && !menuTo.getDishes().isEmpty()) {
            menuRepository.removeById(menu.getId());
            menuTo.setId(null);
            return createMenu(menuTo);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    //TODO возможно лучше удалять меню по menuID
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        menuRepository.removeById(id);
    }

    //TODO не забыть убрать эти методы
    @GetMapping
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }
}
