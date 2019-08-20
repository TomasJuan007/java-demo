package com.example.mybatisdemo.util;

public class SqlProvider {
	public String selectRecordByID(){
		return "select * from Record where id = #{id}";
	}
}
