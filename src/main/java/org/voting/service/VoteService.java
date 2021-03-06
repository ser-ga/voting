package org.voting.service;

import org.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {

    void vote(int restaurantId, String username);

    List<Vote> getAll(String email);

    Vote findByUser_EmailAndDate( String email, LocalDate date);

}
