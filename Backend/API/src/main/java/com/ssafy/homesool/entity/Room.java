package com.ssafy.homesool.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	
	@Column(nullable = true, updatable = false)
	private Date startTime;
	
	@Column
	private Date endTime;
	
	@Column(nullable = false)
	private String code;
	
	public void updateEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(nullable = true)
	private String roomName;
	
	@Column(nullable = false,  columnDefinition = "TINYINT", length=1)
	private int isPublic;
	
	@OneToMany(
			cascade = CascadeType.ALL,
			mappedBy = "roomId")
	private List<Tag> tags;
	
}
