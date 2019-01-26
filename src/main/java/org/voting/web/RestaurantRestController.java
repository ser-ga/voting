package org.voting.web;

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
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.exception.NotFoundException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantRestController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;

    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        Restaurant restaurant = restaurantRepository.getByIdWithMenuByDate(id, LocalDate.now());
        checkNotFound(restaurant, "No Restaurant found for ID " + id);
        return ResponseEntity.ok(restaurant);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        Restaurant found = restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found for ID " + id));
        found.setName(restaurant.getName());
        found.setCity(restaurant.getCity());
        found.setDescription(restaurant.getDescription());
        restaurantRepository.save(found);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        restaurantRepository.deleteById(id);
    }

}
