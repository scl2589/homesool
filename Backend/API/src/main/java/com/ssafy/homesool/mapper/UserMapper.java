package com.ssafy.homesool.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.entity.User;

@Mapper(componentModel="spring")
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDto.UserInfo to(User user);
}
