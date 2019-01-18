package org.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.voting.model.Vote;
import org.voting.service.VoteService;
import org.voting.util.SecurityUtil;

import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    static final String REST_URL = "/rest/votes";


    private VoteService voteService;

    @Autowired
    public VoteRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/for")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void vote(@RequestParam("restaurantId") Integer restaurantId) {
        String username = SecurityUtil.getAuthUsername();
        voteService.vote(restaurantId, username);
    }

    @GetMapping
    public List<Vote> getAll() {
        String username = SecurityUtil.getAuthUsername();
        return voteService.getAll(username);
    }
}
