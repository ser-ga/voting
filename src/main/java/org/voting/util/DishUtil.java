package org.voting.util;

import org.voting.model.Dish;
import org.voting.to.DishTo;

public class DishUtil {

    private DishUtil() {}

    public static Dish createFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }
}
