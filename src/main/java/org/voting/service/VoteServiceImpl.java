package org.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.model.Restaurant;
import org.voting.model.Vote;
import org.voting.repository.VoteRepository;
import org.voting.repository.restaurant.RestaurantRepository;
import org.voting.util.VoteTime;
import org.voting.util.exception.VoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.voting.util.ValidationUtil.checkNotFound;

@Service
@Transactional(readOnly = true)
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
    @Transactional
    public void vote(int restaurantId, String email) {
        if (LocalTime.now().isAfter(VoteTime.getTime())) {
            throw new VoteException("Time for vote expired at " + voteTime + " by server time");
        }

        LOGGER.info("{} vote for restaurant with id {}", email, restaurantId);

        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        Vote vote = voteRepository.findByUserEmailAndDate(email, LocalDate.now());
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