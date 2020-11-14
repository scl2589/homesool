	package com.ssafy.homesool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.entity.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {

	List<UserRecord> findAllByUserIdAndRoomId(long userId, long roomId);
	
	@Query(value = "select id,user_id,room_id, liquor_name, sum(liquor_limit) liquor_limit "
			+ "from user_record "
			+ "where user_id =:userId "
			+ "group by liquor_name "
			+ "order by liquor_limit desc",
			nativeQuery = true)
	List<UserRecord> getStatistics(@Param("userId") long userId);
	
	@Query(value = "select ur.id, ur.user_id, r.room_id, ur.liquor_name, sum(ur.liquor_limit) liquor_limit "
			+ "from user_record ur join room r on ur.room_id = r.room_id "
			+ "where DATE_FORMAT(r.start_time ,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d') and ur.user_id = :userId "
			+ "group by ur.liquor_name",
			nativeQuery = true)
	List<UserRecord> getStatisticsByDate(@Param("userId") long userId, @Param("date") String date);

}
