package com.ssafy.homesool.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.PhotoDto;
import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.entity.Member;
import com.ssafy.homesool.service.PhotoService;
import com.ssafy.homesool.service.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("room")
@Api(tags = {"Room Controller"})
@Tag(name = "Room Controller", description = "미팅과 관련된 기능")
public class RoomController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final RoomService roomService;
	private final PhotoService photoService;

	@GetMapping("code")
	@ApiOperation(value = "미팅 코드 조회", notes = "미팅 코드로 이 미팅이 유효한지 반환한다", response = String.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private String getValidCode() {
		logger.debug(String.format("get rood valid 호출"));
		return roomService.getCode();
	}
	
	@PutMapping("finish/{roomId}")
	@ApiOperation(value = "미팅 종료", notes = "미팅을 종료하고 종료 시각을 기록한다.", response = String.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Room> finishRoom(
		@ApiParam(value = "미팅 id와 종료 시각",required = true) @PathVariable long roomId){
		logger.debug(String.format("finishRoom Room {%s} 호출",roomId));
		return new ResponseEntity<Room>(roomService.finishRoom(roomId),HttpStatus.OK);
	}
	
	@PostMapping("{code}/host")
	@ApiOperation(value = "진짜 미팅 시작 API, 호스트 정보와 룸 정보 넘겨주기", notes = "초기 정보 업데이트", response = RoomDto.RoomResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<RoomDto.RoomResponse> UpdateHost(
		@ApiParam(value = "방 코드",required = true, example = "A1B2C3D4E5") @PathVariable String code,
		@ApiParam(value = "호스트 유저 id와 시작 시각", required = true) @RequestBody RoomDto.InsertHostInfo insertHostInfo) {
		logger.debug("update host in Room 호출\n" );
		//방 제목 추가
		RoomDto.RoomResponse roomResponse = roomService.addBycode(code, insertHostInfo);
		//방에 태그 추가
		roomService.addTags(insertHostInfo.getTags(), roomResponse.getRoomId());
		//호스트 닉네임 업데이트
		roomService.addMember(code, insertHostInfo.getHostId(), insertHostInfo.getHostNickName(), 1);
		return new ResponseEntity<>(roomResponse, HttpStatus.OK);
	}
	
	@PostMapping("{code}/with/{userId}")
	@ApiOperation(value = "미팅 멤버 추가", notes = "미팅에 접속하고 멤버 목록에 추가한 후 response로 roomId를 보내준다.", response = Long.class)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Created"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Long> addMember(
		@ApiParam(value = "방 코드",required = true, example = "A1B2C3D4E5") @PathVariable String code,
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId){
		logger.debug(String.format("add Member {%d} in {%s} 호출",userId,code));
		return new ResponseEntity<>(roomService.addMember(code,userId," ",2),HttpStatus.OK);
	}
	
	@PutMapping("{code}/with/{userId}")
	@ApiOperation(value = "멤버 닉네임 업데이트", notes = "멤버 닉네임 업데이트")
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Created"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Long> UpdateMember(
		@ApiParam(value = "방 코드",required = true, example = "A1B2C3D4E5") @PathVariable String code,
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId,
		@ApiParam(value = "유저 닉네임",required = true, example = "지은짱") @RequestBody RoomDto.UpdateMemberInfo updateMemberInfo){
		logger.debug(String.format("add Member {%d} in {%s} 호출",userId,code));
		return new ResponseEntity<>(roomService.addMember(code,userId,updateMemberInfo.getNickName(),2),HttpStatus.OK);
	}
	
	@PostMapping("photo")
	@ApiOperation(value = "사진 업로드", notes = "서버에 스크린샷을 저장한다.", response = String.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<String> UploadFile(
			@ApiParam(value = "업로드할 사진 정보와 해당 미팅의 id") @RequestBody PhotoDto.PhotoRequest photoRequest
			) {
			logger.debug("사진 업로드 시작\n");
			try {
				photoService.add(photoRequest.getRoomId(),photoRequest.getImg());
			} catch (Exception ex) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(photoRequest.getImg(), HttpStatus.OK);
	}
	
	@GetMapping("{code}")
	@ApiOperation(value = "미팅 코드 조회", notes = "미팅 코드로 이 미팅이 유효한지 반환한다", response = Long.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Long> getRoomValid(
		@ApiParam(value = "room code", required = true, example = "A1B2C3D4E5") @PathVariable String code) {
		logger.debug(String.format("get rood valid %s 호출", code));
		if(roomService.getRoomValid(code) != 0)
			return new ResponseEntity<>(roomService.getRoomValid(code), HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("list/count")
	@ApiOperation(value = "열려있는 미팅 개수 조회", notes = "현재 진행중인 공개방 리스트의 개수를 반환한다", response = Long.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Long> getRoomCount() {
		logger.debug(String.format("get Public Rooms Count 호출"));
		return new ResponseEntity<>(roomService.getPublicRoomsCount(), HttpStatus.OK);	
	}
	
	@GetMapping("list/{pagenum}")
	@ApiOperation(value = "열려있는 미팅 조회", notes = "현재 진행중인 공개방 리스트를 반환한다", response = RoomDto.RoomInfoPlus.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<RoomDto.RoomInfoPlus>> getRoomlist(
			@ApiParam(value = "페이지 번호",required = true, example = "1~") @PathVariable int pagenum) {
		logger.debug(String.format("get Public Rooms 호출"));
		return new ResponseEntity<>(roomService.getPublicRooms(pagenum), HttpStatus.OK);	
	}
	
	@PutMapping
	@ApiOperation(value = "미팅 정보 업데이트", notes = "방제목, 태그, 공개여부 설정", response = RoomDto.RoomResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<RoomDto.RoomInfo> UpdateRoom(
		@ApiParam(value = "방제목,태그,공개여부 설정", required = true) @RequestBody RoomDto.UpdateRoomInfo updateRoomInfo) {
		logger.debug("update Room 호출\n" );
		//방 정보 변경
		RoomDto.RoomInfo roomResponse = roomService.updateRoom(updateRoomInfo);
		//방에 태그 추가
		roomService.addTags(updateRoomInfo.getTags(), updateRoomInfo.getRoomId());
		return new ResponseEntity<>(roomResponse, HttpStatus.OK);
	}

	@GetMapping("list/name/{roomName}/{pagenum}")
	@ApiOperation(value = "열려있는 미팅 조회", notes = "현재 진행중인 공개방 리스트를 방 이름으로 검색한다", response = RoomDto.RoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<RoomDto.RoomInfo>> getRoomlistByName(
			@ApiParam(value = "방 제목",required = true, example = "술게임") @PathVariable String roomName,
			@ApiParam(value = "페이지 번호",required = true, example = "1") @PathVariable int pagenum) {
		logger.debug(String.format("get Public Rooms By Name 호출"));
		return new ResponseEntity<>(roomService.getPublicRoomsByName(roomName, pagenum),HttpStatus.OK);	
	}
	
	@GetMapping("list/tag/{tag}/{pagenum}")
	@ApiOperation(value = "열려있는 미팅 조회", notes = "현재 진행중인 공개방 리스트를 태그로 검색한다", response = RoomDto.RoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<RoomDto.RoomInfo>> getRoomlistByTag(
			@ApiParam(value = "태그 이름",required = true, example = "테스트") @PathVariable String tag,
			@ApiParam(value = "페이지 번호",required = true, example = "1") @PathVariable int pagenum) {
		logger.debug(String.format("get Public Rooms By Tag 호출"));
		return new ResponseEntity<>(roomService.getPublicRoomsByTag(tag, pagenum),HttpStatus.OK);	
	}
	
	@GetMapping("info/{roomId}")
	@ApiOperation(value = "방 정보 조회", notes = "roomId로 해당 방의 이름, 태그, 공개정보를 비롯한 방정보 조회", response = RoomDto.RoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<RoomDto.RoomInfo> getRoomByRoomId(
			@ApiParam(value = "방 번호",required = true, example = "6") @PathVariable long roomId) {
		logger.debug(String.format("get Room Info 호출"));
		return new ResponseEntity<>(roomService.getRoomByRoomId(roomId),HttpStatus.OK);	
	}
	
	@GetMapping("tags")
	@ApiOperation(value = "모든 태그 조회", notes = "현재 등록된 모든 태그 리스트", response = String.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<String>> getAllTags() {
		logger.debug(String.format("get All Tags 호출"));
		return new ResponseEntity<>(roomService.getAllTags(),HttpStatus.OK);	
	}
}
