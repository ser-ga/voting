package org.voting;

import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.voting.model.Restaurant;
import org.voting.model.Role;
import org.voting.model.User;
import org.voting.model.Vote;
import org.voting.util.VoteTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.voting.TestUtil.getContent;
import static org.voting.TestUtil.writeAdditionProps;
import static org.voting.model.AbstractBaseEntity.START_SEQ;
import static org.voting.web.json.JacksonObjectMapper.getMapper;

public class TestData {

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER1_ID = START_SEQ + 1;

    public static final User ADMIN = new User(ADMIN_ID, "Sergey", "admin@yandex.ru", "{noop}pass", new Date(), Role.ROLE_ADMIN);
    public static final User USER1 = new User(USER1_ID, "Ann", "user@ya.ru", "{noop}pass", new Date(), Role.ROLE_USER);
    public static final User USER2 = new User(USER1_ID + 1, "Ann2", "user2@ya.ru", "{noop}pass", new Date(), Role.ROLE_USER);
    public static final User USER3 = new User(USER1_ID + 2, "Ann3", "user3@ya.ru", "{noop}pass", new Date(), Role.ROLE_USER);

    public static final int RESTAURANT1_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "KFC1", "Москва", "Куриные бургеры и картошка");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT1_ID + 1, "KFC2", "Москва", "Куриные бургеры и картошка");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT1_ID + 2, "KFC3", "Москва", "Куриные бургеры и картошка");
    public static final Restaurant RESTAURANT4 = new Restaurant(RESTAURANT1_ID + 3, "McDs1", "Москва", "Бургеры и картошка");
    public static final Restaurant RESTAURANT5 = new Restaurant(RESTAURANT1_ID + 4, "McDs2", "Москва", "Бургеры и картошка");
    public static final Restaurant RESTAURANT6 = new Restaurant(RESTAURANT1_ID + 5, "McDs3", "Москва", "Бургеры и картошка");

    public static final int VOTE1_ID = START_SEQ + 18;

    public static Vote getVote() {
        return new Vote(VOTE1_ID, USER1, RESTAURANT1);
    }

    public static Restaurant getRestaurant() {
        return new Restaurant(null, "Гусли", "Александров", "Ресторан русской кухни");
    }

    public static User getUser() {
        return new User(USER1_ID, USER1.getName(), USER1.getEmail(), USER1.getPassword(), USER1.getRegistered(), USER1.getRoles());
    }

    public static List<Restaurant> getAllRestaurants(Restaurant... restaurants) {
        List<Restaurant> restaurantList = new ArrayList<>(List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3, RESTAURANT4, RESTAURANT5, RESTAURANT6));
        if (restaurants.length > 0) restaurantList.addAll(Arrays.asList(restaurants));
        return restaurantList;
    }

    public static void expireVoteTime() {
        VoteTime.setTime(LocalTime.now().minusSeconds(1));
    }

    public static void increaseVoteTime() {
        VoteTime.setTime(LocalTime.now().plusSeconds(1));
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword().replace("{noop}", ""));
    }

    public static String jsonWithPassword(User user, String passw) {
        return writeAdditionProps(user, "password", passw);
    }

    public static <T> ResultMatcher getToMatcher(Iterable<T> expected, Class<T> clazz) {
        return result -> assertThat(readListFromJsonMvcResult(result, clazz)).isEqualTo(expected);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValues(getContent(result), clazz);
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = getMapper().readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }
}
