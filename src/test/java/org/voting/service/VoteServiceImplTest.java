package org.voting.service;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Restaurant;
import org.voting.model.Vote;
import org.voting.util.VoteTime;
import org.voting.util.exception.VotingExpirationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.voting.TestData.*;
import static org.voting.TestUtil.assertMatch;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceImplTest {

    @Autowired
    private VoteService service;

    @AfterEach
    void tearDown() {
        VoteTime.restore();
    }

    @Test
    void voteTest() {
        increaseVoteTime();
        service.vote(RESTAURANT1_ID, USER1.getEmail());
        Vote stored = service.findByUser_EmailAndDate(USER1.getEmail(), LocalDate.now());
        //TODO т.к. разделяем транзакцию с сервисом, юзера получаем готового, а ресторан в виде прокси обертки
        Restaurant restaurant = (Restaurant) Hibernate.unproxy(stored.getRestaurant());
        assertMatch(stored, getVote(), "user", "restaurant");
        assertMatch(restaurant, RESTAURANT1, "menus", "votes");
        assertMatch(stored.getUser(), USER1, "registered", "votes");
    }

    @Test
    void voteTimeTest() {
        expireVoteTime();
        assertThrows(VotingExpirationException.class, () ->
                service.vote(RESTAURANT1_ID, USER1.getEmail()));
    }
}
