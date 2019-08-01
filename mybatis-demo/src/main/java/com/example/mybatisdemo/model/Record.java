package com.example.mybatisdemo.model;

import java.util.Date;

public class Record {
	private int id;
	private String event;
	private Date date;
	private String location;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "Record [id=" + id + ", event=" + event + ", date=" + date
				+ ", location=" + location + "]";
	}

}
