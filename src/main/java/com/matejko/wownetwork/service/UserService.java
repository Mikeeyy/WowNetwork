package com.matejko.wownetwork.service;

import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.exceptions.SelfFollowingException;
import com.matejko.wownetwork.exceptions.UserAlreadyFollowedException;
import com.matejko.wownetwork.persistance.entity.User;
import com.matejko.wownetwork.persistance.repository.UserRepository;
import com.matejko.wownetwork.shared.mapper.UserMapper;
import com.matejko.wownetwork.shared.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Finds user by nickname
     *
     * @param userNickname user's nickname
     * @return user or empty optional if not found
     */
    public Optional<UserDto> findUserByNickname(final String userNickname) {
        return userRepository.findByNickname(userNickname)
                .map(userMapper::toDto);
    }

    /**
     * Creates a new user
     *
     * @param userNickname user's nickname
     * @return new user
     */
    public UserDto createNewUser(final String userNickname) {
        final User user = new User();
        user.setNickname(userNickname);

        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Starts following given user (toBeFollowedUserName) by another user (followingUserName)
     *
     * @param followingUserName    user that wants to follow another one
     * @param toBeFollowedUserName user that is going to be followed
     *
     * @return model of the user that followed another user
     *
     * @throws NoEntityExistsException      user does not exist
     * @throws UserAlreadyFollowedException user is already followed by the given user
     * @throws SelfFollowingException       user tries to follow himself
     */
    public UserDto follow(final String followingUserName, final String toBeFollowedUserName) throws NoEntityExistsException, UserAlreadyFollowedException, SelfFollowingException {
        if (Objects.equals(followingUserName, toBeFollowedUserName)) {
            throw new SelfFollowingException(followingUserName);
        }

        final User followingUser = userRepository.findByNickname(followingUserName)
                .orElseThrow(() -> new NoEntityExistsException(String.format("No entity for user [%s]", followingUserName)));

        final User toBeFollowedUser = userRepository.findByNickname(toBeFollowedUserName)
                .orElseThrow(() -> new NoEntityExistsException(String.format("No entity for user [%s]", toBeFollowedUserName)));

        if (followingUser.getFollowedUsers().contains(toBeFollowedUser)) {
            throw new UserAlreadyFollowedException(followingUserName, toBeFollowedUserName);
        }

        followingUser.getFollowedUsers().add(toBeFollowedUser);

        return userMapper.toDto(userRepository.save(followingUser));
    }
}
