package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.Dish;
import org.voting.repository.DishRepository;
import org.voting.repository.MenuRepository;
import org.voting.to.DishTo;
import org.voting.util.exception.NotFoundException;

import java.net.URI;
import java.util.List;

import static org.voting.util.DishUtil.createFromTo;
import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    static final String REST_URL = "/rest/dishes";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    public Dish getById(@PathVariable("id") int id) {
        return dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found Dish with id=" + id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody @Validated DishTo dishTo) {
        checkNew(dishTo);
        Dish dish = createFromTo(dishTo);
        dish.setMenu(menuRepository.getOne(dishTo.getMenuId()));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @Validated @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        Dish found = dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Dish not found for ID " + id));
        found.setName(dish.getName());
        found.setPrice(dish.getPrice());
        dishRepository.save(found);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        dishRepository.deleteById(id);
    }

}
