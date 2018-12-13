package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.voting.model.Vote;
import org.voting.service.VoteService;
import org.voting.util.SecurityUtil;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    private VoteService voteService;

    @Autowired
    public VoteRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    // проголосовать за ресторан по ID
    @PostMapping("/{id}")
    public void vote(@PathVariable("id") Integer id) {
        voteService.vote(id, SecurityUtil.getAuthUsername()); // TODO  надо прикрутить авторизацию
    }

    @GetMapping
    public List<Vote> getAll() {
        return voteService.getAllWithUserAndRestaurant();
    }
}