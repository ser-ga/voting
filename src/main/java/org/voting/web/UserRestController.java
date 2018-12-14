package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.voting.model.User;
import org.voting.repository.UserRepository;
import org.voting.util.SecurityUtil;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Integer id) {
        User user = userRepository.getByIdWithRoles(id);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setRegistered(new Date());
        User created = userRepository.save(user);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/users/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        if (userRepository.removeById(id) == 1) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id, @RequestBody User user) {
        User stored = userRepository.findById(id).orElse(null);
        if (stored != null) {
            stored.setName(user.getName());
            stored.setEmail(user.getEmail().toLowerCase());
            stored.setPassword(user.getPassword());
            userRepository.save(stored);
        }
    }

    //TODO get user profile
    @GetMapping("/profile")
    public ResponseEntity getProfile() {
        User authUser = userRepository.getByEmail(SecurityUtil.getAuthUsername());
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    //TODO update user profile
    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProfile(@RequestBody User user) {
        User authUser = userRepository.getByEmail(SecurityUtil.getAuthUsername());
        authUser.setName(user.getName());
        authUser.setEmail(user.getEmail());
        authUser.setPassword(user.getPassword());
        userRepository.save(authUser);
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }
}
