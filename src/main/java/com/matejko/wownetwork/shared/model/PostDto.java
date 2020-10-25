package com.matejko.wownetwork.shared.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ApiModel("Persisted post with identifier")
public class PostDto {
    @ApiModelProperty("Identifier in database")
    String id;

    @ApiModelProperty("Message of the post")
    String message;

    @ApiModelProperty("Date of adding post")
    Date postDate;
}
