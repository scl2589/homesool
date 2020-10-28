package com.ssafy.homesool.service;


import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.RoomDto.InsertRoomInfo;
import com.ssafy.homesool.dto.RoomDto.UpdateRoomInfo;
import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.repository.RoomRepository;

@RequiredArgsConstructor
@Service
public class RoomService {
	private final RoomRepository roomRepository;

	public String add(InsertRoomInfo insertRoomInfo) {
		String code;
		
		//무한 루프 안쓰고 싶은데..
		while (true) {
			//랜덤코드 생성
			code = Util.getRandomCode();
			System.out.println(code);
			//중복 체크
			if(roomRepository.findOneByCode(code) == null) break;
		}
		Room room = Room.builder()
				.hostId(insertRoomInfo.getHostId())
				.startTime(insertRoomInfo.getStartTime())
				.code(code)
				.build();
		return roomRepository.save(room).getCode();
	}

	public Room update(String code, Date endTime) {
		Room room = roomRepository.findOneByCode(code);
		room.updateEndTime(endTime);
		return roomRepository.save(room);
	}
	
	
}
