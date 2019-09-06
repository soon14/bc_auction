package com.bcauction.domain;

import java.math.BigDecimal;

public class Wallet {
	private long wallet_id;
	private String wallet_addr;
	private long wallet_mem;
	private BigDecimal wallet_money = BigDecimal.valueOf(0);
	private int wallet_chargecount = 0;

	public long getWallet_id() {
		return wallet_id;
	}

	public void setWallet_id(long wallet_id) {
		this.wallet_id = wallet_id;
	}

	public String getWallet_addr() {
		return wallet_addr;
	}

	public void setWallet_addr(String wallet_addr) {
		this.wallet_addr = wallet_addr;
	}

	public long getWallet_mem() {
		return wallet_mem;
	}

	public void setWallet_mem(long wallet_mem) {
		this.wallet_mem = wallet_mem;
	}

	public BigDecimal getWallet_money() {
		return wallet_money;
	}

	public void setWallet_money(BigDecimal wallet_money) {
		this.wallet_money = wallet_money;
	}

	public int getWallet_chargecount() {
		return wallet_chargecount;
	}

	public void setWallet_chargecount(int wallet_chargecount) {
		this.wallet_chargecount = wallet_chargecount;
	}

	public boolean isCharge() {
		return this.wallet_chargecount < 10;
	}
}
