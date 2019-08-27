package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletFactory
{
	public static Wallet 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Wallet wallet = new Wallet();
		wallet.setWallet_id(rs.getLong("id"));
		wallet.setWallet_mem(rs.getLong("소유자id"));
		wallet.setWallet_addr(rs.getString("주소"));
		wallet.setWallet_money(rs.getBigDecimal("잔액"));
		wallet.setWallet_chargecount(rs.getInt("충전회수"));

		return wallet;
	}
}
