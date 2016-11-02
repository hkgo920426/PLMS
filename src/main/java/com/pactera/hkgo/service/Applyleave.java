package com.pactera.hkgo.service;

public class Applyleave {
	private int id;
	private String apply_email;
	private String applyfrom;
	private String applyto;
	private int days;
	private String applytype;
	private String applystatus;
	private String applytime;

	public Applyleave() {
	}

	public Applyleave(String apply_email, String applyfrom, String applyto, int days, String applytype, String applystatus, String applytime) {
		this.apply_email = apply_email;
		this.applyfrom = applyfrom;
		this.applyto = applyto;
		this.days = days;
		this.applytype = applytype;
		this.applystatus = applystatus;
		this.applytime = applytime;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApply_email() {
		return apply_email;
	}

	public void setApply_email(String apply_email) {
		this.apply_email = apply_email;
	}

	public String getApplyfrom() {
		return applyfrom;
	}

	public void setApplyfrom(String applyfrom) {
		this.applyfrom = applyfrom;
	}

	public String getApplyto() {
		return applyto;
	}

	public void setApplyto(String applyto) {
		this.applyto = applyto;
	}
	
	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	public String getApplytype() {
		return applytype;
	}

	public void setApplytype(String applytype) {
		this.applytype = applytype;
	}
	
	public String getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(String applystatus) {
		this.applystatus = applystatus;
	}

	public String getApplytime() {
		return applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

}
