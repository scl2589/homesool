package com.ssafy.homesool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {

	List<UserRecord> findAllByUserIdAndRoomId(long userId, long roomId);
	
}
