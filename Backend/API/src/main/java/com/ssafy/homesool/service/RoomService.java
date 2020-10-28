package com.ssafy.homesool.service;


import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.dto.RoomDto.InsertRoomInfo;
import com.ssafy.homesool.entity.Member;
import com.ssafy.homesool.entity.Member;
import com.ssafy.homesool.entity.Room;
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
		Room room= roomRepository.findOneByroomId(roomId);
		room.updateEndTime(endTime);
		return roomRepository.save(room);
	}

	public long addMember(String code, long userId) {
		long roomId = roomRepository.findOneByCode(code).getRoomId();
		Member member = new Member(roomId, userId);
		return memberRepository.save(member).getRoomId();
	}
	
	
}
