package org.voting.util;

import java.time.LocalTime;

public class VoteTime {

    private static LocalTime time;

    private VoteTime(int hour, int minute) {
        time = LocalTime.of(hour, minute);
    }

    public static LocalTime getTime() {
        return time;
    }

    public static void setTime(LocalTime time) {
        VoteTime.time = time;
    }
}
