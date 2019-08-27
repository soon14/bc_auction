package com.bcauction.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {
	private long bid_id;
	private long bid_mem;
	private long bid_auction;
	private LocalDateTime bid_date;
	private BigDecimal bid_price;
	private String bid_issuccess = "N";

	public long getBid_id() {
		return bid_id;
	}

	public void setBid_id(long bid_id) {
		this.bid_id = bid_id;
	}

	public long getBid_mem() {
		return bid_mem;
	}

	public void setBid_mem(long bid_mem) {
		this.bid_mem = bid_mem;
	}

	public long getBid_auction() {
		return bid_auction;
	}

	public void setBid_auction(long bid_auction) {
		this.bid_auction = bid_auction;
	}

	public LocalDateTime getBid_date() {
		return bid_date;
	}

	public void setBid_date(LocalDateTime bid_date) {
		this.bid_date = bid_date;
	}

	public BigDecimal getBid_price() {
		return bid_price;
	}

	public void setBid_price(BigDecimal bid_price) {
		this.bid_price = bid_price;
	}

	public String getBid_issuccess() {
		return bid_issuccess;
	}

	public void setBid_issuccess(String bid_issuccess) {
		this.bid_issuccess = bid_issuccess;
	}

}
