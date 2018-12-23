package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.voting.util.exception.VotingExpirationException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(value = HttpStatus.CONFLICT)  //409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        logExceptionInfo(req, e);
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED) //412 Предварительное условие не выполнено
    @ExceptionHandler({VotingExpirationException.class})
    public void binding(HttpServletRequest req, VotingExpirationException e) {
        logExceptionInfo(req, e);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public void binding(HttpServletRequest req, MethodArgumentNotValidException e) {
        logExceptionInfo(req, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletRequest req, Exception e) {
        logExceptionInfo(req, e);
    }

    private static void logExceptionInfo(HttpServletRequest req, Exception e) {
        Throwable rootCause = getRootCause(e);
        log.error("Error at request " + req.getRequestURL(), rootCause);
    }

    private static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
