package com.ssafy.homesool.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {
	@Id
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, updatable = false, length = 50)
	private String email;
	
	@Column
	private Date birth;
}
