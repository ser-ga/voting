package org.voting.util;

import java.time.LocalTime;

public class VoteTime {
    private static LocalTime VOTE_TIME;

    private VoteTime(String time) {
        VOTE_TIME = LocalTime.parse(time);
    }

    public static LocalTime getVoteTime() {
        return VOTE_TIME;
    }
}
