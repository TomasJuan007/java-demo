package com.example.mybatisdemo;

import com.example.mybatisdemo.model.Record;
import com.example.mybatisdemo.util.MyBaitisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MyTest {

	@Test
	public void testSelect() throws IOException {
		SqlSession session= MyBaitisUtils.openSession();
		String statement="com.example.mybatisdemo.mapper.EventMapper.selectAllEvent";
		List<Record> list = session.selectList(statement);
		System.out.println(list);
		session.close();
	}
}
