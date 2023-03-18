package cn.tzq0301.opensasopenmind.service.impl;

import cn.tzq0301.opensasopenmind.entity.user.User;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.exception.user.IncorrectPasswordException;
import cn.tzq0301.opensasopenmind.exception.user.UsernameAlreadyExistsException;
import cn.tzq0301.opensasopenmind.repository.UserRepository;
import cn.tzq0301.opensasopenmind.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(String username, String password) throws UsernameAlreadyExistsException {
        User user = new User(username, passwordEncoder.encode(password));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Override
    public User isValidUser(String username, String rawPassword) throws AccountNotExistException, IncorrectPasswordException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new AccountNotExistException();
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        return user;
    }
}
