package com.pactera.hkgo.service;

public class Userprofile {
	private int id;
	private String role;
	private String name;
	private String email_address;
	private int leave_balance_annual;
	private int leave_balance_mc;
	private int annual_taken;
	private int mc_taken;
	
	public Userprofile(){		
	}
	
	public Userprofile(int id, String role, String name, String email_address, int leave_balance_annual, int leave_balance_mc, int annual_taken, int mc_taken){
		this.id=id;
		this.role=role;
		this.name=name;
		this.email_address=email_address;
		this.leave_balance_annual=leave_balance_annual;
		this.leave_balance_mc=leave_balance_mc;
		this.annual_taken=annual_taken;
		this.mc_taken=mc_taken;
	}
	
	public int getId(){
		return id;
	}
	
	public String getRole(){
		return role;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail_address(){
		return email_address;
	}
	
	public int getLeave_balance_annual(){
		return	leave_balance_annual;
	}

	public int getLeave_balance_mc(){
		return leave_balance_mc;
	}
	
	public int getAnnual_taken(){
		return annual_taken;
	}
	
	public int getMc_taken(){
		return mc_taken;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public void setRole(String role){
		this.role=role;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setEmail_address(String email_address){
		this.email_address=email_address;
	}
	
	public void setLeave_balance_annual(int leave_balance_annual){
		this.leave_balance_annual=leave_balance_annual;
	}

	public void setLeave_balance_mc(int leave_balance_mc){
		this.leave_balance_mc=leave_balance_mc;
	}
	
	public void setAnnual_taken(int annual_taken){
		this.annual_taken=annual_taken;
	}
	
	public void setMc_taken(int mc_taken){
		this.mc_taken=mc_taken;
	}
}
	
	
	
	
