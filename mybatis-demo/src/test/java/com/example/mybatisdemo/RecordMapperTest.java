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
	public void testSelectEventById() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = mapper.selectEventByID(1);
		session.close();
		System.out.println(record);
	}

	@Test
	public void testSelectAllEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		List<Record> list = mapper.selectAllEvent();
		session.close();
		System.out.println(list);
	}
	
	@Test
	public void testAddEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = new Record();
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setEvent("now");
		record.setDate(sqlDate);
		record.setLocation("Guangzhou");
		mapper.addEvent(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testUpdateEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		Record record = new Record();
		int id = 1;
		record.setId(id);
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setDate(sqlDate);
		record.setLocation("Shenzhen");
		mapper.updateEvent(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testDeleteEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		RecordMapper mapper = session.getMapper(RecordMapper.class);
		int id = 1;
		mapper.deleteEvent(id);
		session.commit();
		session.close();
	}
}
