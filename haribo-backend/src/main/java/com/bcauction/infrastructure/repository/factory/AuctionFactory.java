package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Auction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionFactory {
	public static Auction 생성(ResultSet rs) throws SQLException {
		if (rs == null)
			return null;
		Auction auction = new Auction();

		auction.setAuction_id(rs.getInt("auction_id"));
		auction.setAuction_makedate(rs.getTimestamp("auction_makedate").toLocalDateTime());
		auction.setAuction_makerid(rs.getLong("auction_makerid"));
		auction.setAuction_goodsid(rs.getLong("auction_goodsid"));
		auction.setAuction_status(rs.getString("auction_status"));
		auction.setAuction_start(rs.getTimestamp("auction_start").toLocalDateTime());
		auction.setAuction_end(rs.getTimestamp("auction_end").toLocalDateTime());
		auction.setAuction_min(rs.getBigDecimal("auction_min").toBigInteger());
		auction.setAuction_contract(rs.getString("auction_contranct"));

		return auction;
	}
}
