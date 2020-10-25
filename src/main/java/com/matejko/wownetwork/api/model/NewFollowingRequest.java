package com.matejko.wownetwork.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Data of the new following relation")
public class NewFollowingRequest {
    @NotBlank(message = "Following user name cannot be empty")
    @ApiParam("Following user name")
    private String followingUserName;

    @NotBlank(message = "Name of user to be followed cannot be empty")
    @ApiParam("Name of user to be followed")
    private String toBeFollowedUserName;
}
