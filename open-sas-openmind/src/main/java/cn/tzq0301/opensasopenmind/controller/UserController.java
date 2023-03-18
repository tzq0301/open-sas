package cn.tzq0301.opensasopenmind.controller;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.opensasopenmind.auth.TokenCreator;
import cn.tzq0301.opensasopenmind.entity.user.User;
import cn.tzq0301.opensasopenmind.entity.user.login.LoginRequest;
import cn.tzq0301.opensasopenmind.entity.user.login.LoginResponse;
import cn.tzq0301.opensasopenmind.entity.user.register.RegisterRequest;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.exception.user.IncorrectPasswordException;
import cn.tzq0301.opensasopenmind.exception.user.UsernameAlreadyExistsException;
import cn.tzq0301.opensasopenmind.service.UserService;
import org.springframework.web.bind.annotation.*;

import static cn.tzq0301.http.rest.result.ResultCodeEnum.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final TokenCreator tokenCreator;

    public UserController(UserService userService, TokenCreator tokenCreator) {
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws AccountNotExistException, IncorrectPasswordException {
        User user = userService.isValidUser(request.username(), request.password());
        long userId = user.getId();
        String token = tokenCreator.create(userId);
        return new LoginResponse(userId, token);
    }

    @PostMapping("/register")
    public Long register(@RequestBody RegisterRequest request) throws UsernameAlreadyExistsException {
        User user = userService.saveUser(request.username(), request.password());
        return user.getId();
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
}
