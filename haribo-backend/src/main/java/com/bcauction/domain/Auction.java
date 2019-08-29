package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Auction {
	private long auction_id;
	private LocalDateTime auction_makedate;
	private long auction_makerid; // 회원id
	private long auction_goodsid;
	private String auction_status = "V"; // V valid(유효함), C canceled, E ended
	private LocalDateTime auction_start;
	private LocalDateTime auction_end;
	private BigInteger auction_min;
	private String auction_contract;
	
	
	public long getAuction_id() {
		return auction_id;
	}
	public void setAuction_id(long auction_id) {
		this.auction_id = auction_id;
	}
	public LocalDateTime getAuction_makedate() {
		return auction_makedate;
	}
	public void setAuction_makedate(LocalDateTime auction_makedate) {
		this.auction_makedate = auction_makedate;
	}
	public long getAuction_makerid() {
		return auction_makerid;
	}
	public void setAuction_makerid(long auction_makerid) {
		this.auction_makerid = auction_makerid;
	}
	public long getAuction_goodsid() {
		return auction_goodsid;
	}
	public void setAuction_goodsid(long auction_goodsid) {
		this.auction_goodsid = auction_goodsid;
	}
	public String getAuction_status() {
		return auction_status;
	}
	public void setAuction_status(String auction_status) {
		this.auction_status = auction_status;
	}
	public LocalDateTime getAuction_start() {
		return auction_start;
	}
	public void setAuction_start(LocalDateTime auction_start) {
		this.auction_start = auction_start;
	}
	public LocalDateTime getAuction_end() {
		return auction_end;
	}
	public void setAuction_end(LocalDateTime auction_end) {
		this.auction_end = auction_end;
	}
	public BigInteger getAuction_min() {
		return auction_min;
	}
	public void setAuction_min(BigInteger auction_min) {
		this.auction_min = auction_min;
	}
	public String getAuction_contract() {
		return auction_contract;
	}
	public void setAuction_contract(String auction_contract) {
		this.auction_contract = auction_contract;
	}
	
	
	
}
