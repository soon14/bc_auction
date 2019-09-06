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
		art.setArt_id(rs.getLong("art_id"));
		art.setArt_mem(rs.getLong("art_mem"));
		art.setArt_name(rs.getString("art_name"));
		art.setArt_detail(rs.getString("art_detail"));
		art.setArt_status(rs.getString("art_status"));
		art.setArt_isopen(rs.getString("art_isopen"));

		return art;
	}
}
