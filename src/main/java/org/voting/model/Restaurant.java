package org.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @NotBlank
    @Column(name = "CITY")
    private String city;

    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description; // полное описание
    // ресторан передаем с рейтингом

    @NotNull
    @Column(name = "ADDED")
    private Date added;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("added DESC")
    private List<Menu> menus;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", added=" + added +
                ", menus=" + menus +
                '}';
    }
}
