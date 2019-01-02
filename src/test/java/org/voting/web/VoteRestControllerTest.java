package org.voting.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Vote;
import org.voting.service.VoteService;
import org.voting.util.VoteTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.voting.TestData.*;

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
                .with(userHttpBasic(USER1)))
                .andExpect(status().isNoContent());

        List<Vote> actual = voteService.getAll(USER1.getEmail());
        assertEquals(1, actual.size());
        assertEquals(USER1.getEmail(), actual.get(0).getUserEmail());

        int actualRestaurantId = actual.get(0).getRestaurant().getId();
        assertEquals(RESTAURANT1_ID, actualRestaurantId);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testVoteInvalid() throws Exception {
        increaseVoteTime();
        mockMvc.perform(post(REST_URL + 1)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isConflict());
    }

    @Test
    void testVoteAuth() throws Exception {
        mockMvc.perform(post(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }
}