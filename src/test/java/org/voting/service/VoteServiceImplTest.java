package org.voting.service;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.voting.model.Restaurant;
import org.voting.model.Vote;
import org.voting.util.VoteTime;
import org.voting.util.exception.VoteException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.voting.TestData.*;
import static org.voting.TestUtil.assertMatch;


public class VoteServiceImplTest extends AbstractServiceTest {

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
        assertMatch(stored, getVote(), "user", "restaurant");
        Restaurant restaurant = (Restaurant) Hibernate.unproxy(stored.getRestaurant());
        assertMatch(restaurant, RESTAURANT1, "menus", "votes");
        assertMatch(stored.getUserEmail(), USER1.getEmail(), "registered", "votes");
    }

    @Test
    void voteTimeTest() {
        expireVoteTime();
        assertThrows(VoteException.class, () ->
                service.vote(RESTAURANT1_ID, USER1.getEmail()));
    }
}
