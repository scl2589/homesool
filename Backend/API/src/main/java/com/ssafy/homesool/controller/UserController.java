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

import com.ssafy.homesool.dto.LoginDto;
import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
@Api(tags = {"User Controller"})
@Tag(name = "User Controller", description = "유저와 관련된 기능")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserService userService;

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
