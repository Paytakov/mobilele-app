package com.example.mobilele.model.mapper;

import com.example.mobilele.model.dto.UserRegisterDTO;
import com.example.mobilele.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "active", constant = "true")
    UserEntity userDtoToUserEntity(UserRegisterDTO registerDTO);
}
