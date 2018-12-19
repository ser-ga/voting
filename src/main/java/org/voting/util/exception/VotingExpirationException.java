package org.voting.util.exception;

public class VotingExpirationException extends RuntimeException {
    public VotingExpirationException(String message) {
        super(message);
    }
}