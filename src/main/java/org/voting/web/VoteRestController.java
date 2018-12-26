package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.voting.service.VoteService;
import org.voting.util.SecurityUtil;

@RestController
@RequestMapping(value = "/rest/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteRestController.class);

    private VoteService voteService;

    @Autowired
    public VoteRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    // проголосовать за ресторан по ID
    // TODO отдавать исключения в JSON
    @PostMapping("/{restaurantId}")
    public void vote(@PathVariable("restaurantId") Integer restaurantId) {
        String username = SecurityUtil.getAuthUsername();
        voteService.vote(restaurantId, username); // TODO  надо прикрутить авторизацию
    }
}
