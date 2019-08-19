package com.example.mybatisdemo.model;

import java.util.Date;

public class Record {
	private int id;
	private String event;
	private int priority=0;
	private int value=0;
	private int interest=0;
	private int status=0;
	private long estimate=0;
	private Date createTime;
	private Date updateTime;
	private Date deleteTime;
	private int lft=0;
	private int rgt=0;

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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getEstimate() {
		return estimate;
	}

	public void setEstimate(long estimate) {
		this.estimate = estimate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public int getLft() {
		return lft;
	}

	public void setLft(int lft) {
		this.lft = lft;
	}

	public int getRgt() {
		return rgt;
	}

	public void setRgt(int rgt) {
		this.rgt = rgt;
	}

	@Override
	public String toString() {
		return "Record{" +
				"id=" + id +
				", event='" + event + '\'' +
				", priority=" + priority +
				", value=" + value +
				", interest=" + interest +
				", status=" + status +
				", estimate=" + estimate +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", deleteTime=" + deleteTime +
				", lft=" + lft +
				", rgt=" + rgt +
				'}';
	}
}
