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
    // TODO отдавать исключения в JSON
    @PostMapping("/{id}")
    public void vote(@PathVariable("id") Integer id) {
        String username = SecurityUtil.getAuthUsername();
        voteService.vote(id, username); // TODO  надо прикрутить авторизацию
    }

    //TODO не забыть убрать эти методы
    @GetMapping
    public List<Vote> getAll() {
        return voteService.getAllWithUserAndRestaurant();
    }
}
