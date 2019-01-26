package org.voting.util;

import org.voting.model.Menu;
import org.voting.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;

public class MenuUtil {

    private MenuUtil(){}

    public static Menu createFromTo(MenuTo menuTo) {
        Menu menu = new Menu(menuTo.getId(), menuTo.getAdded() == null ? LocalDate.now() : menuTo.getAdded(), new ArrayList<>(), null);
        menuTo.getDishes().forEach(menu::addDish);
        return menu;
    }

    public static MenuTo asTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getDishes(),menu.getAdded());
    }
}
