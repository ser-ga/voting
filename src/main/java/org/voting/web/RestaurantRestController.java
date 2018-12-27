package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.View;
import org.voting.model.Restaurant;
import org.voting.repository.RestaurantRepository;
import org.voting.util.exception.IllegalRequestDataException;

import java.net.URI;
import java.util.List;

import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;
import static org.voting.web.RestaurantRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String REST_URL = "/rest/restaurants";

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRestController.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantRestController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;

    }

    // достать все рестораны
    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    // достать ресторан по ID с меню на сегодня
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        Restaurant restaurant = restaurantRepository.getByIdWithMenus(id);
        if (restaurant == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Restaurant found for ID " + id);
        return ResponseEntity.ok(restaurant);
    }

    // добавить ресторан
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}") //TODO убрать дублирование URI через константу
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    // обновить ресторан
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        Restaurant stored = restaurantRepository.findById(id).orElse(null);
        if (stored != null) {
            stored.setName(restaurant.getName());
            stored.setCity(restaurant.getCity());
            stored.setDescription(restaurant.getDescription());
            restaurantRepository.save(stored);
        } else {
            throw new IllegalRequestDataException("Not Restaurant found for ID " + id);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        if (restaurantRepository.removeById(id) == 1) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }





}
