package org.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voting.util.exception.VotingExpirationException;
import org.voting.model.Vote;
import org.voting.repository.RestaurantRepository;
import org.voting.repository.UserRepository;
import org.voting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.voting.util.VoteTime.getVoteTime;

@Service
public class VoteServiceImpl implements VoteService {

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
        if (LocalTime.now().isBefore(getVoteTime())) {
            Vote vote = voteRepository.findByUser_EmailAndDate(email, LocalDate.now());
            if (vote == null) {
                vote = new Vote();
                vote.setUser(userRepository.getByEmail(email));
            }
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
        } else {
            throw new VotingExpirationException("Time voting expired at " + getVoteTime() + " by server time");
        }
    }

    //TODO не забыть убрать эти методы
    @Override
    public List<Vote> getAllWithUserAndRestaurant() {
        return voteRepository.getAllWithUserAndRestaurant();
    }
}