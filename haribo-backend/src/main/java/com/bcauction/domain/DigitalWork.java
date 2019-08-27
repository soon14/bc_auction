package com.bcauction.domain;

public class DigitalWork {
	private long art_id;
	private String art_name;
	private String art_detail;
	private String art_isopen = "Y";
	private String art_status = "Y";
	private long art_mem;

	public long getArt_id() {
		return art_id;
	}

	public void setArt_id(long art_id) {
		this.art_id = art_id;
	}

	public String getArt_name() {
		return art_name;
	}

	public void setArt_name(String art_name) {
		this.art_name = art_name;
	}

	public String getArt_detail() {
		return art_detail;
	}

	public void setArt_detail(String art_detail) {
		this.art_detail = art_detail;
	}

	public String getArt_isopen() {
		return art_isopen;
	}

	public void setArt_isopen(String art_isopen) {
		this.art_isopen = art_isopen;
	}

	public String getArt_status() {
		return art_status;
	}

	public void setArt_status(String art_status) {
		this.art_status = art_status;
	}

	public long getArt_mem() {
		return art_mem;
	}

	public void setArt_mem(long art_mem) {
		this.art_mem = art_mem;
	}

	@Override
	public String toString() {
		return "DigitalWork [art_id=" + art_id + ", art_name=" + art_name + ", art_detail=" + art_detail
				+ ", art_isopen=" + art_isopen + ", art_status=" + art_status + ", art_mem=" + art_mem + "]";
	}

}
