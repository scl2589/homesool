package com.ssafy.homesool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	
	Room findOneByCode(String code);
	Room findOneByRoomId(long roomId);
	
	@Query(value = "select r.* from room r inner join member m on r.room_id = m.room_id "
			+ "where m.user_id = :userId",
			nativeQuery = true)
	List<Room> getRoomsInfo(@Param("userId") long userId);
	
	@Query(value = "select r.code from room r", nativeQuery = true)
	List<String> findAllCode();
}
