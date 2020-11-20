package com.ssafy.homesool.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.entity.Member;
import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.entity.Tag;
import com.ssafy.homesool.entity.UserDrink;
import com.ssafy.homesool.exception.RoomNotFoundException;
import com.ssafy.homesool.mapper.RoomMapper;
import com.ssafy.homesool.repository.MemberRepository;
import com.ssafy.homesool.repository.RoomRepository;
import com.ssafy.homesool.repository.TagRepository;

@RequiredArgsConstructor
@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final MemberRepository memberRepository;
	private final TagRepository tagRepository;

	
	public RoomDto.RoomResponse addBycode(String code, RoomDto.InsertHostInfo insertHostInfo) {
		Room room = Room.builder()
				.hostId(insertHostInfo.getHostId())
				.code(code)
				.startTime(new Date())
				.isPublic(insertHostInfo.getIsPublic())
				.roomName(insertHostInfo.getRoomName())
				.build();
		return RoomMapper.INSTANCE.toResponse(
				roomRepository.save(room)
		);
	}
	
	//태그 추가
	public void addTags(List<String> tags, long roomId) {
		//기존 태그 삭제
		tagRepository.deleteAllByRoomId(roomId);
		if(tags != null) {		
			//saveAll 사용하면 한번에 넣을 수 있을텐데 잘 모르겠음
			for(String tagname : tags) {
				Tag tag = Tag.builder()
					.roomId(roomId)
					.tagName(tagname)
					.build();
				tagRepository.save(tag);
			}	
		}
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

	//이거 사용안할걸..?
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
	
	public Room finishRoom(long roomId) {
		Room room= roomRepository.findOneByRoomId(roomId);
		System.out.println(room);
		room.updateEndTime(new Date());
		System.out.println(room);
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
	
	public List<RoomDto.RoomInfoPlus> getPublicRooms(int pagenum){
		List<RoomDto.RoomInfoPlus> roomlist = new ArrayList<>();
		List<RoomDto.RoomInfo> roominfolist = RoomMapper.INSTANCE.toInfo(roomRepository.getPublicRoomsInfo((pagenum-1)*12,pagenum*12));
		for(int i=0; i<roominfolist.size(); i++) {
			RoomDto.RoomInfoPlus roominfoplus = new RoomDto.RoomInfoPlus();
			roominfoplus.setRoominfo(roominfolist.get(i));
			roominfoplus.setHost(memberRepository.findHostnameByroomId(roominfoplus.getRoominfo().getRoomId()));
			roominfoplus.setUsers(memberRepository.findNicknameByroomId(roominfoplus.getRoominfo().getRoomId()));
			roomlist.add(roominfoplus);
		}
		return roomlist;
	}

	public RoomDto.RoomInfo updateRoom(RoomDto.UpdateRoomInfo updateRoomInfo) {
		Room room= roomRepository.findOneByRoomId(updateRoomInfo.getRoomId());
		Room newroom = Room.builder()
				.hostId(room.getHostId())
				.startTime(room.getStartTime())
				.code(room.getCode())
				.roomId(room.getRoomId())
				.roomName(updateRoomInfo.getRoomName())
				.isPublic(updateRoomInfo.getIsPublic())
				.build();
		return RoomMapper.INSTANCE.toInfoOne(roomRepository.save(newroom));
	}
	
	public List<RoomDto.RoomInfo> getPublicRoomsByName(String roomName) {
		return RoomMapper.INSTANCE.toInfo(roomRepository.getPublicRoomsByRoomName(roomName));
	}
	
	public List<RoomDto.RoomInfo> getPublicRoomsByTag(String tag) {
		return RoomMapper.INSTANCE.toInfo(roomRepository.getPublicRoomsByTag(tag));
	}

}
