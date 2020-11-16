package com.ssafy.homesool.service;

import org.springframework.stereotype.Service;

import com.ssafy.homesool.entity.Photo;
import com.ssafy.homesool.repository.PhotoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PhotoService {
	private final PhotoRepository photoRepository;
	
	public void add(long roomId2, String src2) {
		Photo photo = Photo.builder()
				.roomId(roomId2)
				.src(src2)
				.build();
		photoRepository.save(photo);
	}
}
