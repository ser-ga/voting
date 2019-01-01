package org.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Restaurant;
import org.voting.model.Vote;
import org.voting.repository.UserRepository;
import org.voting.repository.VoteRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.VoteTime;
import org.voting.util.exception.VotingExpirationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.voting.util.ValidationUtil.checkNotFound;

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

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        checkNotFound(restaurant, "Not found restaurant with id=" + restaurantId);

        if (LocalTime.now().isBefore(VoteTime.getTime())) {
            Vote vote = voteRepository.findByUser_EmailAndDate(email, LocalDate.now());
            if (vote == null) {
                vote = new Vote();
                vote.setUser(userRepository.getByEmail(email));
            }
            vote.setRestaurant(restaurant);
            voteRepository.save(vote);
        } else {
            throw new VotingExpirationException("Time voting expired at " + voteTime + " by server time");
        }
    }

    @Override
    public List<Vote> getAll(String email) {
        return voteRepository.getAllByUser_Email(email);
    }

    public Vote findByUser_EmailAndDate( String email, LocalDate date){
        return voteRepository.findByUser_EmailAndDate(email, date);
    }
}