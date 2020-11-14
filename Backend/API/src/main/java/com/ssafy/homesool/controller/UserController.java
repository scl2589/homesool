package com.ssafy.homesool.controller;

import java.util.List;

import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.dto.LoginDto;
import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.service.RoomService;
import com.ssafy.homesool.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
@Api(tags = {"User Controller"})
@Tag(name = "User Controller", description = "유저와 관련된 기능")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserService userService;
	private final RoomService roomService;
	
	@PostMapping("login")
	@ApiOperation(value = "카카오 로그인", notes = "카카오 API를 이용한 로그인", response = LoginDto.Response.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<LoginDto.Response> login(
		@ApiParam(required = true) @RequestBody LoginDto.AccessToken accessToken) {
		logger.debug(String.format("login with {%s}호출", accessToken.getAccessToken()));
		return new ResponseEntity<>(userService.login(accessToken.getAccessToken()), HttpStatus.OK);
	}

	@GetMapping("{userId}")
	@ApiOperation(value = "회원 정보 조회", notes = "유저 id로 개인정보를 조회한다.", response = UserDto.UserInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<UserDto.UserInfo> getUserInfo(
		@ApiParam(value = "유저 id", required = true, example = "1404739104") @PathVariable long userId) {
		logger.debug(String.format("get info with %d 호출", userId));
		return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
	}

	@PutMapping("{userId}")
	@ApiOperation(value = "회원 정보 수정", notes = "유저 id로 개인정보를 수정한다.", response = UserDto.UserInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<UserDto.UserInfo> updateUserInfo(
		@ApiParam(value = "유저 id", required = true, example = "1404739104") @PathVariable long userId,
		@ApiParam(value = "업데이트할 유저 정보", required = true) @RequestBody UserDto.UserRequest userReq) {
		logger.debug(String.format("update info with %d 호출", userId));
		return new ResponseEntity<>(userService.update(userId, userReq), HttpStatus.OK);
	}
	
	@GetMapping("{userId}/rooms")
	@ApiOperation(value = "미팅 기록 조회", notes = "미팅 참여기록을 날짜별로 보여준다.", response = RoomDto.RoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<RoomDto.RoomInfo>> getRoomsInfo(
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId){
		logger.debug(String.format("get Room {%d} 호출",userId));
		return new ResponseEntity<>(roomService.get(userId),HttpStatus.OK);
	}
	
	@GetMapping("{userId}/statistics10days")
	@ApiOperation(value = "음주 기록 조회", notes = "음주 기록 통계를 날짜별로 보여준다.", response = UserDto.UserRecord3.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<UserDto.UserRecord3>>  getStatistics10days(
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId){
		logger.debug(String.format("get Statistics {%d} 호출",userId));
		return new ResponseEntity<>(userService.getStatistics10days(userId),HttpStatus.OK);
	}
	
	@GetMapping("{userId}/statistics")
	@ApiOperation(value = "음주 기록 조회", notes = "음주 기록 통계를 날짜별로 보여준다.", response = UserDto.UserRecord2.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<List<UserDto.UserRecord2>> getStatistics(
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId){
		logger.debug(String.format("get Statistics {%d} 호출",userId));
		return new ResponseEntity<>(userService.getStatistics(userId),HttpStatus.OK);
	}
	
	@GetMapping("{userId}/room/{roomId}")
	@ApiOperation(value = "미팅 정보 상세 조회", notes = "상세한 미팅 정보를 조회한다")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<UserDto.UserRecordDetail> getRecord(
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId,
		@ApiParam(value = "미팅 id",required = true, example = "1") @PathVariable long roomId){
		logger.debug(String.format("get Record {%d} room {%d} 호출",userId,roomId));
		return new ResponseEntity<>(userService.getRecord(userId, roomId),HttpStatus.OK);
	}
	
	@PutMapping("{userId}/record/{roomId}")
	@ApiOperation(value = "음주 기록 추가/수정", notes = "실시간 음주량을 갱신한다")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Long> updateRecord(
		@ApiParam(value = "유저 id",required = true, example = "1404739104") @PathVariable long userId,
		@ApiParam(value = "미팅 id",required = true, example = "1") @PathVariable long roomId,
		@ApiParam(value = "음주 기록",required = true) @RequestBody UserDto.UserRecord userRecord){
		logger.debug(String.format("update Record {%d} room {%d} 호출",userId,roomId));
		return new ResponseEntity<>(userService.updateRecord(userId, roomId, userRecord),HttpStatus.OK);
	}
	
	@DeleteMapping("{userId}")
	@ApiOperation(value = "회원 탈퇴", notes = "유저 id로 정보를 삭제한다.")
	@ApiResponses(value = {
		@ApiResponse(code = 204, message = "No Content"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<?> withdrawal(
		@ApiParam(value = "유저 id", required = true, example = "1404739104") @PathVariable long userId) {
		logger.debug(String.format("withdrawal with %d 호출", userId));
		userService.withdrawal(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
