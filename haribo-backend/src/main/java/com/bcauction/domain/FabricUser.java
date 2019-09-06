package com.bcauction.domain;

import org.apache.milagro.amcl.RSA2048.public_key;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.Serializable;
import java.util.Set;

/**
 * TODO 클래스를 완성한다.
 * 패브릭 네트워크 초기 세팅을 위해
 * org.hyperledger.fabric.sdk.User를 implements한 클래스가 필요하다.
 */
public class FabricUser implements User, Serializable
{
	private static final long serialVersionUID = 8077132186383604355L;
	private String name;
	private Set<String>roles;
	private String account;
	private String afflication;
	private Enrollment enrollment;
	private String mspid;
	

	public FabricUser() {
		
	}
	
	public String getAfflication() {
		return afflication;
	}

	public void setAfflication(String afflication) {
		this.afflication = afflication;
	}

	public String getMspid() {
		return mspid;
	}

	public void setMspid(String mspid) {
		this.mspid = mspid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<String> getRoles() {
		return this.roles;
	}

	@Override
	public String getAccount() {
		return this.account;
	}

	@Override
	public String getAffiliation() {
		return this.afflication;
	}

	@Override
	public Enrollment getEnrollment() {
		return this.enrollment;
	}

	@Override
	public String getMspId() {
		return this.mspid;
	}
}
