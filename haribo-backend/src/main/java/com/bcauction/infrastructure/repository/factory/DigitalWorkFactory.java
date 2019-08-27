package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.DigitalWork;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DigitalWorkFactory
{
	public static DigitalWork 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		DigitalWork art = new DigitalWork();
		art.setArt_id(rs.getLong("id"));
		art.setArt_mem(rs.getLong("회원id"));
		art.setArt_name(rs.getString("이름"));
		art.setArt_detail(rs.getString("설명"));
		art.setArt_status(rs.getString("상태"));
		art.setArt_isopen(rs.getString("공개여부"));

		return art;
	}
}
