package org.voting.exception;

public class VotingExpirationException extends RuntimeException {
    public VotingExpirationException(String message) {
        super(message);
    }
}