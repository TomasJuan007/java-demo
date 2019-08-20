package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.model.Record;
import com.example.mybatisdemo.util.SqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


public interface RecordMapper {
	@SelectProvider(type= SqlProvider.class,method="selectRecordByID")
	Record selectRecordByID(int id);
	List<Record> selectAllRecord();
	void addRecord(Record record);
	void updateRecord(Record record);
	void deleteRecord(int id);
}
