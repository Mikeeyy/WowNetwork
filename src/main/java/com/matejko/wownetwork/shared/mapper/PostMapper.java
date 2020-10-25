package com.matejko.wownetwork.shared.mapper;

import com.matejko.wownetwork.persistance.entity.Post;
import com.matejko.wownetwork.shared.model.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto(Post post);
}
