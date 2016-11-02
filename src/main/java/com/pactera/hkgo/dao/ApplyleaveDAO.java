package com.pactera.hkgo.dao;

import java.util.List;

import com.pactera.hkgo.service.Applyleave;

// Defines DAO operations for the applyleave model

public interface ApplyleaveDAO {
	
	public void saveOrUpdate(Applyleave applyleave, String email);
	
	public void delete(int applyId);
	
	public Applyleave get(int applyId);
	
	public void approve(int applyId);
	
	public List<Applyleave> list(String email);
}
