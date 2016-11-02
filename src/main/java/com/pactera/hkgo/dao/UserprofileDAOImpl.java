package com.pactera.hkgo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.pactera.hkgo.service.Applyleave;
import com.pactera.hkgo.service.Userprofile;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * An implementation of the UserprofileDAO interface.
 *
 *
 */

public class UserprofileDAOImpl implements UserprofileDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public UserprofileDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void registerUserprofile(String email_address, String name) {
		String sql = "INSERT INTO userprofilesdb (name, email_address)"
				+ " VALUES (?, ?)";
		jdbcTemplate.update(sql, name, email_address);
		
	}
	
	@Override
	public void setUserprofile(Userprofile userprofile) {
		// update
		String sql = "UPDATE userprofilesdb SET name=?, role=?, leave_balance_annual=?, leave_balance_mc=?, annual_taken=?, mc_taken=?, last_update=CURRENT_TIMESTAMP() "
						+ "WHERE user_id=?";
		jdbcTemplate.update(sql, userprofile.getName(), userprofile.getRole(), userprofile.getLeave_balance_annual(), userprofile.getLeave_balance_mc(), userprofile.getAnnual_taken(), userprofile.getMc_taken(), userprofile.getId());
	
	}
	
	@Override
	public Userprofile getUserprofile( String email_address, String name){
		String sql = "SELECT * FROM userprofilesdb WHERE email_address=\""+ email_address+"\"";
		return jdbcTemplate.query(sql, new ResultSetExtractor<Userprofile>() {

			@Override
			public Userprofile extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Userprofile userprofile = new Userprofile();
					userprofile.setId(rs.getInt("user_id"));
					userprofile.setName(rs.getString("name"));
					userprofile.setRole(rs.getString("role"));
					userprofile.setEmail_address(rs.getString("email_address"));
					userprofile.setLeave_balance_annual(rs.getInt("leave_balance_annual"));
					userprofile.setLeave_balance_mc(rs.getInt("leave_balance_mc"));
					userprofile.setAnnual_taken(rs.getInt("annual_taken"));
					userprofile.setMc_taken(rs.getInt("mc_taken"));
					return userprofile;
				}
				
				registerUserprofile(email_address,name);
				return null;
			}
	
		});
	}

	@Override
	public List<Userprofile> list(String email) {
		String sql;
			
		if (email==null){
			sql = "SELECT * FROM userprofilesdb";
		}
			
		else{
			sql = "SELECT * FROM userprofilesdb where email_address LIKE \"%"+ email+"%\"";
		}
			
		List<Userprofile> listUserprofile = jdbcTemplate.query(sql, new RowMapper<Userprofile>() {

			@Override
			public Userprofile mapRow(ResultSet rs, int rowNum) throws SQLException {
				Userprofile uUserprofile = new Userprofile();
	
				uUserprofile.setId(rs.getInt("user_id"));
				uUserprofile.setName(rs.getString("name"));
				uUserprofile.setRole(rs.getString("role"));
				uUserprofile.setEmail_address(rs.getString("email_address"));
				uUserprofile.setLeave_balance_annual(rs.getInt("leave_balance_annual"));
				uUserprofile.setLeave_balance_mc(rs.getInt("leave_balance_mc"));
				uUserprofile.setAnnual_taken(rs.getInt("annual_taken"));
				uUserprofile.setMc_taken(rs.getInt("mc_taken"));
				
				return uUserprofile;
			}
			
		});
		
		return listUserprofile;
	}
	
	
	
	@Override
	public Userprofile get(int userprofileId) {
		String sql = "SELECT * FROM userprofilesdb WHERE user_id=" + userprofileId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Userprofile>() {

			@Override
			public Userprofile extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Userprofile userprofile = new Userprofile();
					userprofile.setId(rs.getInt("user_id"));
					userprofile.setName(rs.getString("name"));
					userprofile.setRole(rs.getString("role"));
					userprofile.setEmail_address(rs.getString("email_address"));
					userprofile.setLeave_balance_annual(rs.getInt("leave_balance_annual"));
					userprofile.setLeave_balance_mc(rs.getInt("leave_balance_mc"));
					userprofile.setAnnual_taken(rs.getInt("annual_taken"));
					userprofile.setMc_taken(rs.getInt("mc_taken"));
					return userprofile;
				}
				
				return null;
			}
			
		});
		
		
	}
	
	@Override
	public List<String> getAdminlist() {
		String	sql = "SELECT email_address FROM userprofilesdb where role=\"Admin\"";
					
		List<String> adminlist = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				
	
				String admin_email=rs.getString("email_address");
				
				return admin_email;
			}
			
		});
		
		return adminlist;
	}
	
	
}