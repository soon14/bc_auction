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
		wallet.setWallet_id(rs.getLong("wallet_id"));
		wallet.setWallet_mem(rs.getLong("wallet_mem"));
		wallet.setWallet_addr(rs.getString("wallet_addr"));
		wallet.setWallet_money(rs.getBigDecimal("wallet_money"));
		wallet.setWallet_chargecount(rs.getInt("wallet_chargecount"));

		return wallet;
	}
}
