package com.ssafy.homesool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.dto.RoomDto;
import com.ssafy.homesool.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
