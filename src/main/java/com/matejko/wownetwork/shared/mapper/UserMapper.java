package com.matejko.wownetwork.shared.mapper;

import com.matejko.wownetwork.persistance.entity.User;
import com.matejko.wownetwork.shared.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
