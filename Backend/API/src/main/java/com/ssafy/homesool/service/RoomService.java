package com.ssafy.homesool.service;


import java.util.Date;
import java.util.HashSet;
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
		// 무한루프는 그대로지만 HashSet 활용해서 DB 접근 최소화!
		HashSet<String> set = new HashSet<>(roomRepository.findAllCode());
		while (true) {
			code = Util.getRandomCode();
			if(!set.contains(code)) break;
		}
		
		Room room = Room.builder()
				.hostId(insertRoomInfo.getHostId())
				.startTime(insertRoomInfo.getStartTime())
				.code(code)
				.roomName("")
				.build();
		return RoomMapper.INSTANCE.toResponse(
				roomRepository.save(room)
		);
	}
	
	public RoomDto.RoomResponse addBycode(String code, long hostId, String roomName) {
		Room room = Room.builder()
				.hostId(hostId)
				.code(code)
				.roomName(roomName)
				.build();
		return RoomMapper.INSTANCE.toResponse(
				roomRepository.save(room)
		);
	}
	
	public String getCode() {
		String code;
		// 무한루프는 그대로지만 HashSet 활용해서 DB 접근 최소화!
		HashSet<String> set = new HashSet<>(roomRepository.findAllCode());
		while (true) {
			code = Util.getRandomCode();
			if(!set.contains(code)) break;
		}
		
		return code;
	}


	public Room updateRoomName(long roomId, String roomName) {
		Room room= roomRepository.findOneByRoomId(roomId);
		Room newroom = Room.builder()
				.hostId(room.getHostId())
				.startTime(room.getStartTime())
				.code(room.getCode())
				.roomId(room.getRoomId())
				.roomName(roomName)
				.build();
		return roomRepository.save(newroom);
	}
	
	public Room update(long roomId, Date endTime) {
		Room room= roomRepository.findOneByRoomId(roomId);
		room.updateEndTime(endTime);
		return roomRepository.save(room);
	}

	public long addMember(String code, long userId, String nickName, int ishost) {
		
		Room room = roomRepository.findOneByCode(code);
		// Room Not Found or Room is closed
		if(room == null || room.getEndTime() != null) 
			throw new RoomNotFoundException(code);
		Member member = new Member(room.getRoomId(), userId, nickName, ishost);
		return memberRepository.save(member).getRoomId();
	}

	public List<RoomDto.RoomInfo> get(long userId) {
		return RoomMapper.INSTANCE.toInfo(roomRepository.getRoomsInfo(userId));
	}
	
	public long getRoomValid(String code) {
		Room room = roomRepository.findOneByCode(code);
		if(room != null) 
			return room.getRoomId();
		else
			return 0;
	}
	
}
