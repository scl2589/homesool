package com.ssafy.homesool.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	Room findOneByCode(String code);
	Room findOneByroomId(long roomId);
	
}
