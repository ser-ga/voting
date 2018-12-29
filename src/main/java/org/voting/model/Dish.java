package org.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {

    @NotNull
    @Column(name = "PRICE", precision=10, scale=2)
    private BigDecimal price;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;

    public Dish() {
    }

    public Dish(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Dish(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
        this.menu = menu;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dish dish = (Dish) o;
        return Objects.equals(price, dish.price) &&
                Objects.equals(menu, dish.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), price, menu);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
