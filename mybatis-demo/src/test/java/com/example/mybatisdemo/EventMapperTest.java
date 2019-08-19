package com.example.mybatisdemo;

import com.example.mybatisdemo.mapper.EventMapper;
import com.example.mybatisdemo.model.Record;
import com.example.mybatisdemo.util.MyBaitisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class EventMapperTest {

	@Test
	public void testSelectEventById() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		EventMapper mapper = session.getMapper(EventMapper.class);
		Record record = mapper.selectEventByID(1);
		session.close();
		System.out.println(record);
	}

	@Test
	public void testSelectAllEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		EventMapper mapper = session.getMapper(EventMapper.class);
		List<Record> list = mapper.selectAllEvent();
		session.close();
		System.out.println(list);
	}
	
	@Test
	public void testAddEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		EventMapper mapper = session.getMapper(EventMapper.class);
		Record record = new Record();
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setEvent("Guangzhou");
		record.setCreateTime(sqlDate);
		mapper.addEvent(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testUpdateEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		EventMapper mapper = session.getMapper(EventMapper.class);
		Record record = new Record();
		int id = 1;
		record.setId(id);
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		record.setCreateTime(sqlDate);
		record.setEvent("Shenzhen");
		mapper.updateEvent(record);
		session.commit();
		session.close();
	}
	
	@Test
	public void testDeleteEvent() throws IOException {
		SqlSession session = MyBaitisUtils.openSession();
		EventMapper mapper = session.getMapper(EventMapper.class);
		int id = 1;
		mapper.deleteEvent(id);
		session.commit();
		session.close();
	}
}
