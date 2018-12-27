package org.voting.web.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.User;
import org.voting.web.AbstractControllerTest;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.voting.TestData.*;
import static org.voting.TestUtil.*;
import static org.voting.model.Role.ROLE_USER;
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
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, User.class), USER1, "password", "registered"))
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
        List<User> expectList = List.of(ADMIN, USER2, USER3);
        assertMatch(userRepository.findAll(), expectList, "registered", "votes");
    }

    @Test
    void testRegister() throws Exception {
        User created = new User(null, "newName", "newemail@ya.ru", "newPassword");
        ResultActions action = mockMvc.perform(post(REST_URL + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(created, "newPassword")))
                .andDo(print())
                .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);
        returned.setPassword(created.getPassword());
        created.setId(returned.getId());
        assertMatch(returned, created, "registered", "votes", "roles");
        created.setRoles(Set.of(ROLE_USER));
        assertMatch(userRepository.getByEmail("newemail@ya.ru"), created, "registered", "votes");
    }

    @Test
    void testUpdate() throws Exception {
        User updated = new User(USER1.getId(), "newName", "newemail@ya.ru", "newPassword");
        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1))
                .content(jsonWithPassword(updated, "newPassword")))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userRepository.getByEmail("newemail@ya.ru"), updated, "registered", "votes", "roles");
    }

    @Test
    void testUpdateInvalid() throws Exception {
        User updated = new User();

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1))
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testDuplicate() throws Exception {
        User updated = new User(null, "newName", "admin@yandex.ru", "newPassword");

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1))
                .content(jsonWithPassword(updated, "newPassword")))
                .andExpect(status().isConflict())
                .andDo(print());
    }
}