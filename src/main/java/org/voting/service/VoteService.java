package org.voting.service;

import org.voting.model.Vote;

import java.util.List;

public interface VoteService {

    void vote(int restaurantId, String username);

    List<Vote> getAllWithUserAndRestaurant();

}