package org.voting.to;

import org.voting.HasId;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DishTo implements HasId {

    protected Integer id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer menuId;


    public DishTo(Integer id, String name, BigDecimal price, Integer menuId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }

    public DishTo() {
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
