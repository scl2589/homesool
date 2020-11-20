package com.ssafy.homesool.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.entity.Room;

@Mapper
public interface RoomMapper {
	RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);
	
	RoomDto.RoomResponse toResponse(Room room);
	List<RoomDto.RoomInfo> toInfo(List<Room> rooms);
	RoomDto.RoomInfo toInfoOne(Room room);
}
