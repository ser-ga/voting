package org.voting.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.voting.model.Vote;
import org.voting.service.VoteService;
import org.voting.util.VoteTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.voting.TestData.*;
import static org.voting.TestUtil.assertMatch;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @AfterEach
    void tearDown() {
        VoteTime.restore();
    }

    @Test
    void testVote() throws Exception {
        increaseVoteTime();
        mockMvc.perform(post(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER1)));

        List<Vote> actual = voteService.findAll();

        assertEquals( 1, actual.size());
        assertMatch(actual.get(0).getUser(), USER1, "votes", "registered");

        int actualRestaurantId = actual.get(0).getRestaurant().getId();

        assertEquals(RESTAURANT1_ID, actualRestaurantId);
    }

    @Test
    void testVoteInvalid() throws Exception {
        increaseVoteTime();
        mockMvc.perform(post(REST_URL + 1)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testVoteAuth() throws Exception {
        mockMvc.perform(post(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }
}