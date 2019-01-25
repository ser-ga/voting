package org.voting.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.AuthorizedUser;
import org.voting.View;
import org.voting.model.Role;
import org.voting.model.User;
import org.voting.repository.UserRepository;

import java.net.URI;
import java.util.Collections;

import static org.voting.util.UserUtil.prepareToSave;
import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL)
public class ProfileRestController {
    static final String REST_URL = "/rest/profiles";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(Authentication authentication) {
        AuthorizedUser authorizedUser = (AuthorizedUser) authentication.getPrincipal();
        return authorizedUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication) {
        AuthorizedUser authorizedUser = (AuthorizedUser) authentication.getPrincipal();
        userRepository.deleteById(authorizedUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Validated(View.Web.class) @RequestBody User user) {
        checkNew(user);
        prepareToSave(user, passwordEncoder);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        User created = userRepository.saveAndFlush(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody User user, Authentication authentication) {
        AuthorizedUser authorizedUser = (AuthorizedUser) authentication.getPrincipal();
        assureIdConsistent(user, authorizedUser.getId());
        prepareToSave(user, passwordEncoder);
        userRepository.save(user);
    }
}