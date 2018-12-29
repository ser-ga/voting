package org.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static org.voting.TestData.*;
import static org.voting.TestUtil.assertMatch;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class MenuServiceImplTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        Menu created = new Menu(null, LocalDate.now(), getNewDishes(), RESTAURANT1);
        menuService.create(created, RESTAURANT1_ID);
        assertMatch(menuService.getAll(), List.of(MENU1, MENU2, MENU3, created), "restaurant", "dishes");
    }

    @Test
    void getById() {
        Menu actual = menuService.getById(MENU1_ID);
        assertMatch(actual, MENU1, "restaurant", "dishes");
        assertMatch(MENU1.getDishes(), actual.getDishes(), "menu");
    }

    @Test
    void getBy() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }
}