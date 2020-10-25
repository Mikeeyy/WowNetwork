package com.matejko.wownetwork.shared.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ApiModel("Persisted user that is being followed")
public class FollowedUserDto {
    @ApiModelProperty("Identifier in database")
    String id;

    @ApiModelProperty("User's nick")
    String nickname;

    @ApiModelProperty("Posts the user has created")
    List<PostDto> posts;
}
