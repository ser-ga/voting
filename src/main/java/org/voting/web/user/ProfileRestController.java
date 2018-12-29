package org.voting.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.View;
import org.voting.model.Role;
import org.voting.model.User;
import org.voting.repository.UserRepository;
import org.voting.util.SecurityUtil;

import java.net.URI;
import java.util.Collections;

import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL)
public class ProfileRestController {
    static final String REST_URL = "/rest/profile";

    private final UserRepository userRepository;

    @Autowired
    public ProfileRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return userRepository.getByEmail(SecurityUtil.getAuthUsername());
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete() {
        userRepository.deleteByEmail(SecurityUtil.getAuthUsername());
    }

    //TODO проверить валидацию
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Validated(View.Web.class) @RequestBody User user) {
        checkNew(user);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        User created = userRepository.saveAndFlush(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody User user) {
        User stored = userRepository.getByEmail(SecurityUtil.getAuthUsername());
        assureIdConsistent(user, stored.getId());
        stored.setEmail(user.getEmail());
        stored.setPassword(user.getPassword());
        stored.setName(user.getName());
        userRepository.save(stored);
    }
}