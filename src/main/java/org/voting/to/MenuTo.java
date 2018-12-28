package org.voting.to;

import org.voting.HasId;
import org.voting.model.Dish;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MenuTo implements HasId {
    
    protected Integer id;
    
    @NotNull
    private Integer restaurantId;

    @NotNull
    private List<Dish> dishes;

    private LocalDate date;
    
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


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo menuTo = (MenuTo) o;
        return restaurantId.equals(menuTo.restaurantId) &&
                Objects.equals(id, menuTo.id) &&
                Objects.equals(dishes, menuTo.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId, dishes);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", dishes=" + dishes +
                '}';
    }
}
