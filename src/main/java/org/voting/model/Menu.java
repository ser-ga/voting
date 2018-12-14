package org.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu extends AbstractBaseEntity {

    @NotNull
    @Column(name = "ADDED")
    private LocalDate added;

    //TODO вытаскиваем меню с едой
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    @OrderBy("name ASC")
    private List<Dish> dishes;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
        this.added = added;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Menu() {
    }

    public Menu(@NotNull LocalDate added,  Restaurant restaurant) {
        this.added = added;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", added=" + added +
                ", dishes=" + dishes +
                ", restaurant=" + restaurant +
                '}';
    }
}
