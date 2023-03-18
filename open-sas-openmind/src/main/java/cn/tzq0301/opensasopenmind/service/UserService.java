package cn.tzq0301.opensasopenmind.service;

import cn.tzq0301.opensasopenmind.entity.user.User;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.exception.user.IncorrectPasswordException;
import cn.tzq0301.opensasopenmind.exception.user.UsernameAlreadyExistsException;

public interface UserService {
    User saveUser(String username, String password) throws UsernameAlreadyExistsException;

    User isValidUser(String username, String rawPassword) throws AccountNotExistException, IncorrectPasswordException;
}
