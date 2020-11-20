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
			+ "where m.user_id = :userId and room_name not like ''",
			nativeQuery = true)
	List<Room> getRoomsInfo(@Param("userId") long userId);
	
	@Query(value = "select r.code from room r", nativeQuery = true)
	List<String> findAllCode();
	
	@Query(value = "select DATE_FORMAT(start_time ,'%Y-%m-%d') date "
			+ "from room r join user_record ur on r.room_id = ur.room_id "
			+ "where user_id = :userId "
			+ "group by DATE_FORMAT(start_time ,'%Y-%m-%d') "
			+ "limit 10",
			nativeQuery = true)
	List<String> get10days(@Param("userId") long userId);
	
	@Query(value = "select * from room "
			+ "where is_public = 1 and end_time is NULL "
			+ "lIMIT :start,:end",
			nativeQuery = true)
	List<Room> getPublicRoomsInfo(@Param("start") int start, @Param("end") int end);

	@Query(value = "select *"
			+ "from room "
			+ "where is_public = 1 and end_time is NULL and room_name like %:roomName% "
			+ "lIMIT :start,:end",
			nativeQuery = true)
	List<Room> getPublicRoomsByRoomName(@Param("roomName") String roomName, @Param("start") int start, @Param("end") int end);
	
	@Query(value = "select r.is_public, r.room_id, r.start_time, r.end_time, r.host_id, r.code, r.room_name, t.tag_name "
			+ "from room r join tag t on r.room_id = t.room_id "
			+ "where r.is_public = 1 and r.end_time is NULL and t.tag_name like %:tag% "
			+ "group by r.room_id "
			+ "lIMIT :start,:end",
			nativeQuery = true)	
	List<Room> getPublicRoomsByTag(@Param("tag") String tag, @Param("start") int start, @Param("end") int end);
	
	@Query(value = "select COUNT(*) from room "
			+ "where is_public = 1 and end_time is NULL",
			nativeQuery = true)
	long getPublicRoomsCount();

}
