package org.voting.web.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.voting.model.Role;
import org.voting.model.User;
import org.voting.web.AbstractControllerTest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.voting.TestData.*;
import static org.voting.TestUtil.assertMatch;
import static org.voting.TestUtil.readFromJsonResultActions;
import static org.voting.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.id", is(USER1.getId())))
                .andExpect(jsonPath("$.name", is(USER1.getName())))
                .andExpect(jsonPath("$.email", is(USER1.getEmail())))
                .andExpect(jsonPath("$.enabled", is(USER1.isEnabled())))
                .andDo(print());
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isNoContent());
        assertMatch(new String[]{"registered", "votes"}, userRepository.findAll(), ADMIN, USER2, USER3);
    }

    @Test
    void testRegister() throws Exception {
        User created = new User(null, "newName", "newemail@ya.ru", "newPassword", null, Role.ROLE_USER);
        ResultActions action = mockMvc.perform(post(REST_URL + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(created, "newPassword")))
                .andDo(print())
                .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);
        returned.setPassword(created.getPassword());
        created.setId(returned.getId());
        assertMatch(returned, created, "registered", "votes", "roles");
        assertMatch(userRepository.getByEmail("newemail@ya.ru"), created,"registered", "votes");
    }

    @Test
    void update() {
                User byEmail = userRepository.getByEmail("user@ya.ru");

    }
}