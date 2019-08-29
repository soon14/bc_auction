package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class AuctionInfo {
	private String aucInfo_contract; 		// 경매 컨트랙트 주
	private BigInteger aucInfo_highest; 	// 최고 입찰액
	private long aucInfo_artId; 			// 작품id
	private long aucInfo_highestBider; 		// 최고입찰자id
	private LocalDateTime aucInfo_start; 	// 경매시작시간
	private LocalDateTime aucInfo_end; 		// 경매종료시간
	private BigInteger aucInfo_min; 		// 최소금액
	private boolean aucInfo_close; 			// 종료

	public String getAucInfo_contract() {
		return aucInfo_contract;
	}

	public void setAucInfo_contract(String aucInfo_contract) {
		this.aucInfo_contract = aucInfo_contract;
	}

	public BigInteger getAucInfo_highest() {
		return aucInfo_highest;
	}

	public void setAucInfo_highest(BigInteger aucInfo_highest) {
		this.aucInfo_highest = aucInfo_highest;
	}

	public long getAucInfo_artId() {
		return aucInfo_artId;
	}

	public void setAucInfo_artId(long aucInfo_artId) {
		this.aucInfo_artId = aucInfo_artId;
	}

	public long getAucInfo_highestBider() {
		return aucInfo_highestBider;
	}

	public void setAucInfo_highestBider(long aucInfo_highestBider) {
		this.aucInfo_highestBider = aucInfo_highestBider;
	}

	public LocalDateTime getAucInfo_start() {
		return aucInfo_start;
	}

	public void setAucInfo_start(LocalDateTime aucInfo_start) {
		this.aucInfo_start = aucInfo_start;
	}

	public LocalDateTime getAucInfo_end() {
		return aucInfo_end;
	}

	public void setAucInfo_end(LocalDateTime aucInfo_end) {
		this.aucInfo_end = aucInfo_end;
	}

	public BigInteger getAucInfo_min() {
		return aucInfo_min;
	}

	public void setAucInfo_min(BigInteger aucInfo_min) {
		this.aucInfo_min = aucInfo_min;
	}

	public boolean isAucInfo_close() {
		return aucInfo_close;
	}

	public void setAucInfo_close(boolean aucInfo_close) {
		this.aucInfo_close = aucInfo_close;
	}

}
