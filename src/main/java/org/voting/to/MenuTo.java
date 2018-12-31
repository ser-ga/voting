package org.voting.to;

import org.springframework.format.annotation.DateTimeFormat;
import org.voting.HasId;
import org.voting.model.Dish;
import org.voting.model.Menu;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class MenuTo implements HasId {

    protected Integer id;

    @NotNull
    private Integer restaurantId;

    @NotNull
    private List<Dish> dishes;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate added;

    public MenuTo() {
    }

    public MenuTo(Integer restaurantId, List<Dish> dishes) {
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }

    public MenuTo(Integer id, Integer restaurantId, List<Dish> dishes, LocalDate added) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishes = dishes;
        this.added = added;
    }

    public static MenuTo fromMenu(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getDishes(), menu.getAdded());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", dishes=" + dishes +
                ", added=" + added +
                '}';
    }
}
