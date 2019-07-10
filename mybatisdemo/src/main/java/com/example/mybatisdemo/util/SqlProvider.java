package com.example.mybatisdemo.util;

public class SqlProvider {
	public String selectEventByID(){
		return "select * from Record where id = #{id}";
	}
}
