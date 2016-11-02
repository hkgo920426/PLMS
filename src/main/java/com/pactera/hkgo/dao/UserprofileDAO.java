package com.pactera.hkgo.dao;

import java.util.List;

import com.pactera.hkgo.service.Userprofile;

// Defines DAO operations for the userprofile model

public interface UserprofileDAO {
	
	public void registerUserprofile (String email_address, String name);
	public void setUserprofile(Userprofile userprofile);
	public Userprofile getUserprofile(String email_address, String name);
	public List<Userprofile> list(String email);
	public Userprofile get(int userprofileId);
	public List<String> getAdminlist();
	
	

	
	
}
