package com.ssafy.homesool.entity;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Getter
@Embeddable
public class MemberId implements Serializable{

	private long roomId;
	private long userId;
	
	public MemberId() {
		
	}
	
	public MemberId(long roomId, long userId) {
		this.roomId = roomId;
		this.userId = userId;
	}
}
