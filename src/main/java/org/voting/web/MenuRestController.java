package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.Menu;
import org.voting.service.MenuService;
import org.voting.to.MenuTo;
import org.voting.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.voting.util.MenuUtil.createFromTo;
import static org.voting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
    static final String REST_URL = "/rest/menus";

    private final MenuService menuService;

    @Autowired
    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Validated @RequestBody MenuTo menuTo) {
        menuTo.getDishes().forEach(ValidationUtil::checkNew);

        Menu created = createFromTo(menuTo);
        Menu stored = menuService.create(created, menuTo.getRestaurantId());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(stored.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(stored);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(menuService.getById(id));
    }

    @GetMapping(value = "/by")
    public ResponseEntity getBy(@RequestParam("restaurantId") int restaurantId,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "date", required = false) LocalDate date) {
        return ResponseEntity.ok().body(menuService.getBy(restaurantId, date));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<MenuTo> getAll() {
        return menuService.getAll().stream()
                .map(MenuTo::fromMenu)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") int id, @Validated @RequestBody MenuTo menuTo) {
        assureIdConsistent(menuTo, id);
        Menu updated = createFromTo(menuTo);
        Menu menu = menuService.update(updated, menuTo.getRestaurantId());

        return ResponseEntity.ok().body(menu);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        menuService.delete(id);
    }

}
