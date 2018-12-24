package org.voting;

import org.voting.model.Restaurant;
import org.voting.model.Role;
import org.voting.model.User;
import org.voting.model.Vote;
import org.voting.util.VoteTime;

import java.time.LocalTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.voting.model.AbstractBaseEntity.START_SEQ;

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

    public static <T> void assertMatch(T actual, T expected, String... ignoringFields) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoringFields);
    }

    public static void expireVoteTime() {
        VoteTime.setTime(LocalTime.now().minusSeconds(1));
    }

    public static void increaseVoteTime() {
        VoteTime.setTime(LocalTime.now().plusSeconds(1));
    }
}
