package com.matejko.wownetwork.api;


import com.matejko.wownetwork.api.model.NewFollowingRequest;
import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.exceptions.SelfFollowingException;
import com.matejko.wownetwork.exceptions.UserAlreadyFollowedException;
import com.matejko.wownetwork.service.UserService;
import com.matejko.wownetwork.shared.model.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
@Api(description = "Set of endpoints related to operations regarding users")
public class UserController {
    private final UserService userService;

    @PostMapping("/follow")
    @ApiOperation("Sets the user A as follower of user B")
    public UserDto follow(@RequestBody @Valid @ApiParam("Data of the new following relation") final NewFollowingRequest request) throws NoEntityExistsException, UserAlreadyFollowedException, SelfFollowingException {
        return userService.follow(request.getFollowingUserName(), request.getToBeFollowedUserName());
    }
}
