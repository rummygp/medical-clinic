package com.rummgp;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity user);

    UserDto toDto(User user);

    UserEntity toEntity(UserCreateCommand userCreateCommand);

    User toPojo(UserCreateCommand userCreateCommand);

    User toPojo(UserEntity userEntity);

    UserEntity toEntity(User user);

    UserFindCommand toCommand(UserFindRequestDto userFindRequestDto);
}
