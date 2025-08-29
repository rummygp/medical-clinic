package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.UserCreateCommand;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserCreateCommand userCreateCommand);
}
