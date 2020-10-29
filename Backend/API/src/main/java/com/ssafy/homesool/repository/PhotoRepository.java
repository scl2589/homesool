package com.ssafy.homesool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.homesool.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>{

}
