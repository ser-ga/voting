package org.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.voting.util.exception.IllegalRequestDataException;
import org.voting.util.exception.NotFoundException;
import org.voting.util.exception.VoteException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(value = HttpStatus.CONFLICT)  //409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        logExceptionInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED) //412 Предварительное условие не выполнено
    @ExceptionHandler({VoteException.class})
    public void binding(HttpServletRequest req, VoteException e) {
        logExceptionInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalRequestDataException.class, BindException.class, NotFoundException.class})
    public void binding(HttpServletRequest req, Exception e) {
        logExceptionInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND) //404
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public void noResult(HttpServletRequest req, EmptyResultDataAccessException e) {
        logExceptionInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN) //403
    @ExceptionHandler({AccessDeniedException.class})
    public void accessDenied(HttpServletRequest req, AccessDeniedException e) {
        logExceptionInfo(req, e, false);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletRequest req, Exception e) {
        logExceptionInfo(req, e, true);
    }

    private static void logExceptionInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = getRootCause(e);
        String errorString = "Error at request " + req.getRequestURL();
        if (logException) {
            LOGGER.error(errorString, rootCause);
        } else {
            LOGGER.warn(errorString, rootCause);
        }
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
