package org.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {

    //хранить будем только дату голосования, время 11-00 будем только проверять
    @NotNull
    @Column(name = "DATE")
    private LocalDate date = LocalDate.now();

    @NotNull
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote(){}

    public Vote(Integer id, String userEmail, Restaurant restaurant) {
        super(id);
        this.userEmail = userEmail;
        this.restaurant = restaurant;
    }

    public Vote(String userEmail, Restaurant restaurant) {
        this.userEmail = userEmail;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + userEmail +
                ", restaurant=" + restaurant +
                '}';
    }
}
