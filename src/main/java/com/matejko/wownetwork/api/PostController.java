package com.matejko.wownetwork.api;

import com.matejko.wownetwork.api.model.NewPostRequest;
import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.service.PostService;
import com.matejko.wownetwork.service.UserService;
import com.matejko.wownetwork.shared.model.PostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Api(description = "Set of endpoints used creating and retrieving posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping
    @ApiOperation("Creates a new post, if the user does not exists the method creates it")
    public PostDto createNewPost(@RequestBody @Valid @ApiParam("Data of the new post") final NewPostRequest request) throws NoEntityExistsException {
        final var user = userService.findUserByNickname(request.getUserNickname())
                .orElseGet(() -> userService.createNewUser(request.getUserNickname()));

        return postService.createPost(user.getNickname(), request.getPostMessage());
    }

    @GetMapping("/wall/{userNickname}")
    @ApiOperation("Retrieves user's list of the posted messages, in reverse chronological order")
    public List<PostDto> getUsersWall(@PathVariable @ApiParam("User's nick") final String userNickname) {
        return postService.getPostsByUserNickname(userNickname);
    }

    @GetMapping("/timeline/{userNickname}")
    @ApiOperation("Retrieves list of the posted messages by the people the user is following, in reverse chronological order")
    public List<PostDto> getTimeline(@PathVariable @ApiParam("User's nick") final String userNickname) {
        return postService.getTimelineByUserNickname(userNickname);
    }
}
