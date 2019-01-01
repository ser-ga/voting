package org.voting.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.View;
import org.voting.model.User;
import org.voting.repository.UserRepository;
import org.voting.util.exception.NotFoundException;

import java.net.URI;
import java.util.List;

import static org.voting.util.UserUtil.prepareToSave;
import static org.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {

    static final String REST_URL = "/rest/users";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Integer id) {
        return checkNotFound(userRepository.getById(id), "User not found with ID=" + id);
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.getAllBy();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Validated(View.Web.class) @RequestBody User user) {
        checkNew(user);
        prepareToSave(user, passwordEncoder);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        if (userRepository.removeById(id) == 0) throw new NotFoundException("User not found with ID=" + id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id, @Validated(View.Web.class) @RequestBody User user) {
        assureIdConsistent(user, id);
        prepareToSave(user, passwordEncoder);
        userRepository.save(user);
    }

    @GetMapping("/by")
    public User getByEmail(@RequestParam("email") String email) {
        return checkNotFound(userRepository.getByEmail(email),"User not found with email=" + email);
    }
}
