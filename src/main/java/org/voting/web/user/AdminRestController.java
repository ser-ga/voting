package org.voting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.User;
import org.voting.repository.UserRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "/rest/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRestController.class);

    private final UserRepository userRepository;

    @Autowired
    public AdminRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Integer id) {
        User user = userRepository.getByIdWithRoles(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }


    //TODO проверить как создаются роли
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        checkNew(user);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        if (userRepository.removeById(id) == 1) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    //TODO переделать через ExceptionHandler
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id, @Valid @RequestBody User user) {
        assureIdConsistent(user, id);
        userRepository.save(user);
    }
}
