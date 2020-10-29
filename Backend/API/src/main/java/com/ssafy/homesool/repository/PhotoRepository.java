package com.ssafy.homesool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>{
	
	@Query(value = "select src from photo "
			+ "where room_id = :roomId ",
			nativeQuery = true)
	List<String> findSrcByroomId(@Param("roomId") long roomId);
}
