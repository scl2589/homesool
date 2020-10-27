package com.ssafy.homesool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.UserDrink;

@Repository
public interface UserDrinkRepository extends JpaRepository<UserDrink, Long> {
	
	@Transactional
	long deleteAllByUserId(long userId);

	List<UserDrink> findByUserId(long userId);
}
