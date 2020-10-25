package com.matejko.wownetwork.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Data of the new post")
public class NewPostRequest {
    @NotBlank(message = "User's nick cannot be empty")
    @ApiParam("User's nick")
    private String userNickname;

    @ApiParam("Post message")
    @NotBlank(message = "Post message cannot be empty")
    @Length(max = 140, message = "Maximum number of characters for the post is 140")
    private String postMessage;
}
