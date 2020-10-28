package com.ssafy.homesool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	Room findOneByCode(String code);
	
}
