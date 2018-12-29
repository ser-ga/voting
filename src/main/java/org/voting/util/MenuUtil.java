package org.voting.util;

import org.voting.model.Menu;
import org.voting.to.MenuTo;

import java.time.LocalDate;

public class MenuUtil {

    public static Menu createFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getAdded() == null ? LocalDate.now() : menuTo.getAdded(),menuTo.getDishes(),null);
    }

    public static MenuTo asTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getDishes(),menu.getAdded());
    }
}
