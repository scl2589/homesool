package com.ssafy.homesool.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long roomId;

	@Column(nullable =false)
	private long hostId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date endTime;
	
	@Column(nullable = false)
	private String code;
	
	public void updateEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(nullable = false)
	private String roomName;
}
