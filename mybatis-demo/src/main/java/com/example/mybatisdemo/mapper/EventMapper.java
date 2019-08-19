package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.model.Record;
import com.example.mybatisdemo.util.SqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


public interface EventMapper {
	@SelectProvider(type= SqlProvider.class,method="selectEventByID")
	Record selectEventByID(int id);
	List<Record> selectAllEvent();
	void addEvent(Record record);
	void updateEvent(Record record);
	void deleteEvent(int id);
}
