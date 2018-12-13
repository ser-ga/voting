package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.Restaurant;
import org.voting.repository.RestaurantRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

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
        Restaurant restaurant = restaurantRepository.getByIdWithMenuOfDay(id, LocalDate.now());
        if (restaurant == null) return new ResponseEntity<>("No Restaurant found for ID " + id, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    // добавить ресторан
    @PostMapping(value = "/admin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody Restaurant restaurant) {
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/restaurants/{id}") //TODO убрать дублирование URI через константу
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    // обновить ресторан
    @PutMapping(value = "/admin/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @RequestBody Restaurant restaurant) {
        Restaurant stored = restaurantRepository.findById(id).orElse(null);
        if (stored != null) {
            stored.setName(restaurant.getName());
            stored.setCity(restaurant.getCity());
            stored.setDescription(restaurant.getDescription());
            restaurantRepository.save(stored);
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        if (restaurantRepository.removeById(id) == 1) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }





}
