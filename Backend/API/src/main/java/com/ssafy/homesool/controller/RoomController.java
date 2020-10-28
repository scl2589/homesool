package com.ssafy.homesool.controller;

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

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.dto.RoomDto.RoomInfo;
import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.service.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("room")
@Api(tags = {"Room Controller"})
@Tag(name = "Room Controller", description = "미팅과 관련된 기능")
public class RoomController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final RoomService roomService;

	@PostMapping
	@ApiOperation(value = "미팅 시작", notes = "새로 미팅을 주최한다.", response = RoomDto.InsertRoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<String> addRoom(
		@ApiParam(value = "추가할 미팅 정보", required = true) @RequestBody RoomDto.InsertRoomInfo insertRoomInfo) {
		logger.debug("add Room 호출\n" + insertRoomInfo.toString());
		return new ResponseEntity<>(roomService.add(insertRoomInfo), HttpStatus.OK);
	}
	
	@PutMapping
	@ApiOperation(value = "미팅 종료", notes = "미팅을 종료하고 종료 시각을 기록한다.", response = RoomDto.UpdateRoomInfo.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	private ResponseEntity<Room> updateRoom(
		@ApiParam(value = "미팅 코드와 종료시각",required = true) @RequestBody RoomDto.UpdateRoomInfo updateRoomInfo){
		logger.debug(String.format("update Room {%s} end time {%s} 호출",updateRoomInfo.getCode(), updateRoomInfo.getEndTime().toString()));
		return new ResponseEntity<Room>(roomService.update(updateRoomInfo.getCode(), updateRoomInfo.getEndTime()),HttpStatus.OK);
	}
}
