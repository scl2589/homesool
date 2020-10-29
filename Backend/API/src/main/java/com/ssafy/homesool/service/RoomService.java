package com.ssafy.homesool.service;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.dto.RoomDto.InsertRoomInfo;
import com.ssafy.homesool.entity.Member;
import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.exception.RoomNotFoundException;
import com.ssafy.homesool.mapper.RoomMapper;
import com.ssafy.homesool.repository.MemberRepository;
import com.ssafy.homesool.repository.RoomRepository;

@RequiredArgsConstructor
@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final MemberRepository memberRepository;

	public RoomDto.RoomResponse add(InsertRoomInfo insertRoomInfo) {
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
		return RoomMapper.INSTANCE.toResponse(
				roomRepository.save(room)
		);
	}

	public Room update(long roomId, Date endTime) {
		Room room= roomRepository.findOneByRoomId(roomId);
		room.updateEndTime(endTime);
		return roomRepository.save(room);
	}

	public long addMember(String code, long userId) {
		
		Room room = roomRepository.findOneByCode(code);
		// Room Not Found or Room is closed
		if(room == null || room.getEndTime() != null) 
			throw new RoomNotFoundException(code);
		Member member = new Member(room.getRoomId(), userId);
		return memberRepository.save(member).getRoomId();
	}

	public List<RoomDto.RoomInfo> get(long userId) {
		return RoomMapper.INSTANCE.toInfo(roomRepository.getRoomsInfo(userId));
	}
	
	
}
