package com.bcauction.domain;

import java.time.LocalDateTime;

public class Ownership {
	private long own_id;
	private long own_mem;
	private long own_art;
	private LocalDateTime own_start;
	private LocalDateTime own_end;

	public long getOwn_id() {
		return own_id;
	}

	public void setOwn_id(long own_id) {
		this.own_id = own_id;
	}

	public long getOwn_mem() {
		return own_mem;
	}

	public void setOwn_mem(long own_mem) {
		this.own_mem = own_mem;
	}

	public long getOwn_art() {
		return own_art;
	}

	public void setOwn_art(long own_art) {
		this.own_art = own_art;
	}

	public LocalDateTime getOwn_start() {
		return own_start;
	}

	public void setOwn_start(LocalDateTime own_start) {
		this.own_start = own_start;
	}

	public LocalDateTime getOwn_end() {
		return own_end;
	}

	public void setOwn_end(LocalDateTime own_end) {
		this.own_end = own_end;
	}

	@Override
	public String toString() {
		return "Ownership [own_id=" + own_id + ", own_mem=" + own_mem + ", own_art=" + own_art + ", own_start="
				+ own_start + ", own_end=" + own_end + "]";
	}

	
}
