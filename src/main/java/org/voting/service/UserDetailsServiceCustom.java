package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.voting.AuthorizedUser;
import org.voting.model.User;
import org.voting.repository.UserRepository;

@Service("userService")
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthorizedUser loadUserByUsername(String username) {
        User user = userRepository.getByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AuthorizedUser(user);
    }
}
