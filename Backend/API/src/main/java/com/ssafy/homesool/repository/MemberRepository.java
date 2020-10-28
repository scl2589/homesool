package com.ssafy.homesool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
