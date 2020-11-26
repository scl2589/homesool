package com.ssafy.homesool.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

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
@IdClass(MemberId.class)
public class Member implements Serializable{
	@Id
	@Column(nullable = false, updatable = false)
	private long roomId;
	@Id
	@Column(nullable = false, updatable = false)
	private long userId;
	
	@Column(nullable = true)
	private String nickname;
	
	@Column(nullable = false, updatable = false)
	private int ishost;
}
