package com.matejko.wownetwork.service;

import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.exceptions.SelfFollowingException;
import com.matejko.wownetwork.exceptions.UserAlreadyFollowedException;
import com.matejko.wownetwork.persistance.entity.User;
import com.matejko.wownetwork.persistance.repository.UserRepository;
import com.matejko.wownetwork.shared.mapper.UserMapper;
import com.matejko.wownetwork.shared.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static com.matejko.wownetwork.service.TestHelper.userEntity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        final var userMapper = spy(Mappers.getMapper(UserMapper.class));

        userService = new UserService(repository, userMapper);
    }

    @Test
    @DisplayName("Should return found user")
    void findUserByNicknameSuccess() {
        final var entity = userEntity();
        final var nickname = entity.getNickname();

        when(repository.findByNickname(nickname)).thenReturn(of(entity));

        final var dto = userService.findUserByNickname(nickname);

        assertThat(dto)
                .isPresent()
                .get()
                .extracting(UserDto::getId, UserDto::getNickname)
                .containsExactly(entity.getId(), nickname);
    }

    @Test
    @DisplayName("Should return empty optional for not found user")
    void findUserByNicknameFailure() {
        when(repository.findByNickname(any())).thenReturn(empty());

        final var dto = userService.findUserByNickname(UUID.randomUUID().toString());

        assertThat(dto).isEmpty();
    }

    @Test
    @DisplayName("Should create new user")
    void createNewUser() {
        final var entity = userEntity();

        when(repository.save(any())).thenReturn(entity);

        final var dto = userService.createNewUser(entity.getNickname());

        assertThat(dto)
                .extracting(UserDto::getId, UserDto::getNickname)
                .containsExactly(entity.getId(), entity.getNickname());
    }

    @Test
    @DisplayName("Following should pass for correct data")
    void followSuccess() throws NoEntityExistsException, UserAlreadyFollowedException, SelfFollowingException {
        final var user1 = userEntity();
        final var user2 = userEntity();

        when(repository.findByNickname(user1.getNickname())).thenReturn(of(user1));
        when(repository.findByNickname(user2.getNickname())).thenReturn(of(user2));

        final var argument = ArgumentCaptor.forClass(User.class);

        when(repository.save(argument.capture())).thenAnswer(invocation -> argument.getValue());

        final var user1OneFollow = userService.follow(user1.getNickname(), user2.getNickname());

        assertThat(user1OneFollow)
                .isNotNull()
                .extracting(
                        UserDto::getId,
                        UserDto::getNickname,
                        userDto -> userDto.getFollowedUsers().size(),
                        userDto -> userDto.getFollowedUsers().get(0).getId(),
                        userDto -> userDto.getFollowedUsers().get(0).getNickname()
                )
                .containsExactly(
                        user1.getId(),
                        user1.getNickname(),
                        1,
                        user2.getId(),
                        user2.getNickname()
                );
    }

    @Test
    @DisplayName("Following should throw if entity has not been found")
    void followExceptionNoEntity() {
        final var user1 = userEntity();
        final var user2 = userEntity();

        when(repository.findByNickname(user1.getNickname())).thenReturn(of(user1));
        when(repository.findByNickname(user2.getNickname())).thenReturn(empty());

        assertThrows(NoEntityExistsException.class,
                () -> userService.follow(user1.getNickname(), user2.getNickname()));
    }

    @Test
    @DisplayName("Following should throw if user has already been followed")
    void followExceptionAlreadyFollowed() {
        final var user1 = userEntity();

        final var user2 = userEntity();
        user1.getFollowedUsers().add(user2);

        when(repository.findByNickname(user1.getNickname())).thenReturn(of(user1));
        when(repository.findByNickname(user2.getNickname())).thenReturn(of(user2));

        assertThrows(UserAlreadyFollowedException.class,
                () -> userService.follow(user1.getNickname(), user2.getNickname()));
    }

    @Test
    @DisplayName("Following should throw if user tries to follow himself")
    void followExceptionSelfFollowing() {
        final var name = "Adam";

        assertThrows(SelfFollowingException.class,
                () -> userService.follow(name, name));
    }
}