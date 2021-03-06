package org.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voting.model.Restaurant;
import org.voting.model.Vote;
import org.voting.repository.VoteRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.VoteTime;
import org.voting.util.exception.VoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final VoteTime voteTime;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, RestaurantRepository restaurantRepository, VoteTime voteTime) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteTime = voteTime;
    }

    @Override
    public void vote(int restaurantId, String email) {
        LOGGER.info("{} vote for restaurant with id {}", email, restaurantId);
        Vote vote = voteRepository.findByUserEmailAndDate(email, LocalDate.now());

        if (vote != null && restaurantId != vote.getRestaurant().getId() && LocalTime.now().isAfter(VoteTime.getTime())) {
            throw new VoteException("Time for change vote expired at " + voteTime + " by server time");
        }

        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        if (vote == null) {
            vote = new Vote();
            vote.setUserEmail(email);
        }
        vote.setRestaurant(restaurant);
        voteRepository.save(vote);
    }

    @Override
    public List<Vote> getAll(String email) {
        return voteRepository.getAllByUserEmail(email);
    }

    @Override
    public Vote findByUser_EmailAndDate(String email, LocalDate date) {
        return voteRepository.findByUserEmailAndDate(email, date);
    }
}