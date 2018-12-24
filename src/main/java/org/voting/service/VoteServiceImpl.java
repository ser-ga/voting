package org.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Vote;
import org.voting.repository.RestaurantRepository;
import org.voting.repository.UserRepository;
import org.voting.repository.VoteRepository;
import org.voting.util.VoteTime;
import org.voting.util.exception.VotingExpirationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final VoteTime voteTime;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, VoteTime voteTime) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.voteTime = voteTime;
    }

    @Override
    @Transactional
    public void vote(int restaurantId, String email) {
        LOGGER.info("{} vote for restaurant with id {}", email, restaurantId);
        if (LocalTime.now().isBefore(VoteTime.getTime())) {
            Vote vote = voteRepository.findByUser_EmailAndDate(email, LocalDate.now());
            if (vote == null) {
                vote = new Vote();
                vote.setUser(userRepository.getByEmail(email));
            }
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
        } else {
            throw new VotingExpirationException("Time voting expired at " + voteTime + " by server time");
        }
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    public Vote findByUser_EmailAndDate( String email, LocalDate date){
        return voteRepository.findByUser_EmailAndDate(email, date);
    }
}