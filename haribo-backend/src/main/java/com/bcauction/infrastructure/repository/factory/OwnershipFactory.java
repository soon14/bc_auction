package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Ownership;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OwnershipFactory
{
	public static Ownership 생성(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Ownership own = new Ownership();

		own.setOwn_id(rs.getLong("own_id"));
		own.setOwn_mem(rs.getLong("own_mem"));
		own.setOwn_art(rs.getLong("own_art"));
		if(rs.getString("own_start") != null)
			own.setOwn_start(LocalDateTime.parse(rs.getString("own_start")));
		if(rs.getString("own_end") != null)
			own.setOwn_end(LocalDateTime.parse(rs.getString("own_end")));

		return own;
	}
}
