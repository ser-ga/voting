package org.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Filter;
import org.hibernate.validator.constraints.SafeHtml;
import org.voting.View;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @NotBlank
    @Column(name = "CITY")
    @SafeHtml(groups = {View.Web.class})
    private String city;

    @NotBlank
    @Column(name = "DESCRIPTION")
    @SafeHtml(groups = {View.Web.class})
    private String description;

    @NotNull
    @Column(name = "ADDED")
    private LocalDate added  = LocalDate.now();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("added DESC")
    @Filter(name="added", condition = "added = :added")
    private List<Menu> menus = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, String city, String description) {
        super(id, name);
        this.city = city;
        this.description = description;
    }

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

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
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
                ", id=" + id +
                ", name='" + name + '\'' +
                "city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", added=" + added +
                '}';
    }
}
