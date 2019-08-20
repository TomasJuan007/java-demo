package com.example.mybatisdemo;

import com.example.mybatisdemo.mapper.RecordMapper;
import com.example.mybatisdemo.model.Record;
import com.example.mybatisdemo.util.MyBaitisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RecordMapperTest {

	@Test
	public void testSelectRecordById() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = mapper.selectRecordByID(1);
		session.close();
		System.out.println(record);
	}

	@Test
	public void testSelectAllRecord() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		List<Record> list = mapper.selectAllRecord();
		session.close();
		System.out.println(list);
	}
	
	@Test
	public void testAddRecord() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = new Record();
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setEvent("Guangzhou");
		record.setCreateTime(sqlDate);
		mapper.addRecord(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testUpdateRecord() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = new Record();
		int id = 1;
		record.setId(id);
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setCreateTime(sqlDate);
		record.setEvent("Shenzhen");
		mapper.updateRecord(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testDeleteRecord() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		int id = 1;
		mapper.deleteRecord(id);
		session.commit();
		session.close();
	}
}
