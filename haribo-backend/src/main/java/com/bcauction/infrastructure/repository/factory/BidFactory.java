package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Bid;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidFactory
{
	public static Bid 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Bid bid = new Bid();
		bid.setBid_id(rs.getLong("bid_id"));
		bid.setBid_mem(rs.getLong("bid_mem"));
		bid.setBid_auction(rs.getLong("bid_auction"));
		bid.setBid_date(rs.getTimestamp("bid_date").toLocalDateTime());
		bid.setBid_price(rs.getBigDecimal("bid_price"));
		bid.setBid_issuccess(rs.getString("bid_issuccess"));

		return bid;
	}
}
