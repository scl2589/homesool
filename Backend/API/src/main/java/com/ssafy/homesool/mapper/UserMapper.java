package com.ssafy.homesool.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.entity.User;
import com.ssafy.homesool.entity.UserRecord;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDto.UserInfo to(User user);
	List<UserDto.UserRecord2> toRecord(List<UserRecord> userRecords);
	List<UserDto.UserRecord> toRecordList(List<UserRecord> userRecords);
}
