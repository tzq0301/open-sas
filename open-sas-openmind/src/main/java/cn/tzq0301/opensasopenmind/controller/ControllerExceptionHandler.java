package cn.tzq0301.opensasopenmind.controller;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.opensasopenmind.exception.token.InvalidTokenException;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.exception.user.IncorrectPasswordException;
import cn.tzq0301.opensasopenmind.exception.user.UsernameAlreadyExistsException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

import static cn.tzq0301.http.rest.result.ResultCodeEnum.*;

@RestControllerAdvice
public class ControllerExceptionHandler {
//    @ExceptionHandler(Exception.class) // TODO
    public Result<?> handleException(Exception e) {
        return Result.error(SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleException(HttpMessageNotReadableException e) {
        Throwable cause1 = e.getCause();
        if (cause1 != null) {
            Throwable cause2 = cause1.getCause();
            if (cause2 != null) {
                if (cause2 instanceof DateTimeParseException) {
                    return Result.error(USER_REQUEST_PARAM_ILLEGAL_TIMESTAMP);
                }
            }
        }

        return Result.error(USER_REQUEST_PARAM_ERROR);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public Result<?> handleException(UsernameAlreadyExistsException e) {
        return Result.error(USER_REGISTER_USERNAME_ALREADY_EXISTS);
    }

    @ExceptionHandler(AccountNotExistException.class)
    public Result<?> handleException(AccountNotExistException e) {
        return Result.error(USER_LOGIN_ACCOUNT_NOT_EXIST);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public Result<?> handleException(IncorrectPasswordException e) {
        return Result.error(USER_LOGIN_INCORRECT_PASSWORD);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public Result<?> handleException(InvalidTokenException e) {
        return Result.error(USER_REQUEST_PARAM_ERROR, "Token is invalid");
    }
}
