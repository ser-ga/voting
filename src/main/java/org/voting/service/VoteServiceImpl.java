package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.exception.VotingExpirationException;
import org.voting.model.Vote;
import org.voting.repository.RestaurantRepository;
import org.voting.repository.UserRepository;
import org.voting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    public static final LocalTime END_VOTING_TIME = LocalTime.of(23, 30);

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void vote(int restaurantId, String email) {
        if (LocalTime.now().isBefore(END_VOTING_TIME)) {
            Vote vote = voteRepository.findByUser_EmailAndDate(email, LocalDate.now());
            if (vote == null) {
                vote = new Vote();
                vote.setUser(userRepository.getByEmail(email));
            }
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
        } else {
            throw new VotingExpirationException("Time voting expired at 11:00 by server time");
        }
    }

    @Override
    public List<Vote> getAllWithUserAndRestaurant() {
        return voteRepository.getAllWithUserAndRestaurant();
    }
}