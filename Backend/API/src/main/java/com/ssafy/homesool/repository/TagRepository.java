package com.ssafy.homesool.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
	
	@Transactional
	long deleteAllByRoomId(long roomId);
}
