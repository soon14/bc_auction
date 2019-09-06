package com.bcauction.domain;

import java.time.LocalDateTime;

public class Member {
	private long mem_id;
	private String mem_name;
	private String mem_mail;
	private String mem_pass;
	private LocalDateTime mem_registdate;

	public long getMem_id() {
		return mem_id;
	}

	public void setMem_id(long mem_id) {
		this.mem_id = mem_id;
	}

	public String getMem_name() {
		return mem_name;
	}

	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}

	public String getMem_mail() {
		return mem_mail;
	}

	public void setMem_mail(String mem_mail) {
		this.mem_mail = mem_mail;
	}

	public String getMem_pass() {
		return mem_pass;
	}

	public void setMem_pass(String mem_pass) {
		this.mem_pass = mem_pass;
	}

	public LocalDateTime getMem_registdate() {
		return mem_registdate;
	}

	public void setMem_registdate(LocalDateTime mem_registdate) {
		this.mem_registdate = mem_registdate;
	}

}
