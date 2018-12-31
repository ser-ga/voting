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

import java.net.URI;
import java.util.List;

import static org.voting.util.UserUtil.prepareToSave;
import static org.voting.util.ValidationUtil.assureIdConsistent;
import static org.voting.util.ValidationUtil.checkNew;

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
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        if (userRepository.removeById(id) == 1) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    //TODO переделать через ExceptionHandler
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id, @Validated(View.Web.class) @RequestBody User user) {
        assureIdConsistent(user, id);
        prepareToSave(user, passwordEncoder);
        userRepository.save(user);
    }

    @GetMapping(value = "/by")
    public ResponseEntity getByEmail(@RequestParam("email") String email) {
        User user = userRepository.getByEmail(email);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
}
