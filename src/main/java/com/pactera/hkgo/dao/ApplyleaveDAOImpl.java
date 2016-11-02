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
 * An implementation of the ApplyleaveDAO interface.
 *
 *
 */
public class ApplyleaveDAOImpl implements ApplyleaveDAO {

	private JdbcTemplate jdbcTemplate;
	
	public ApplyleaveDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveOrUpdate(Applyleave applyleave, String email) {
		if (applyleave.getId() > 0) {
			// update
			String sql = "UPDATE applyleave SET applyfrom=?, applyto=?, days=?, applytype=?, applystatus=?, applytime=CURRENT_TIMESTAMP() "
						+ "WHERE apply_id=?";
			jdbcTemplate.update(sql, applyleave.getApplyfrom(),	applyleave.getApplyto(), applyleave.getDays(), applyleave.getApplytype(), "Pending", applyleave.getId());
		} 
		
		else {
			// insert
			String sql = "INSERT INTO applyleave (apply_email ,applyfrom, applyto, days, applytype)"
						+ " VALUES (?, ?, ?, ? ,?)";
			jdbcTemplate.update(sql, email, applyleave.getApplyfrom(), applyleave.getApplyto(), applyleave.getDays(), applyleave.getApplytype());
		}
		
	}

	@Override
	public void delete(int applyleaveId) {
		String sql = "DELETE FROM applyleave WHERE apply_id=?";
		jdbcTemplate.update(sql, applyleaveId);
	}

	@Override
	public List<Applyleave> list(String email) {
		String sql;
		if (email=="Pending"){
			sql = "SELECT * FROM applyleave where applystatus=\""+"Pending"+"\"";
		}
		else
			sql = "SELECT * FROM applyleave where apply_email=\""+ email+"\""+" ORDER BY applytime DESC";
		
			
		List<Applyleave> listApplyleave = jdbcTemplate.query(sql, new RowMapper<Applyleave>() {

			@Override
			public Applyleave mapRow(ResultSet rs, int rowNum) throws SQLException {
				Applyleave aApplyleave = new Applyleave();
	
				aApplyleave.setId(rs.getInt("apply_id"));
				aApplyleave.setApply_email(rs.getString("apply_email"));
				aApplyleave.setApplyfrom(rs.getString("applyfrom"));
				aApplyleave.setApplyto(rs.getString("applyto"));
				aApplyleave.setDays(rs.getInt("days"));
				aApplyleave.setApplytype(rs.getString("applytype"));
				aApplyleave.setApplystatus(rs.getString("applystatus"));
				aApplyleave.setApplytime(rs.getString("applytime"));
				
				return aApplyleave;
			}
			
		});
		
		return listApplyleave;
	}

	@Override
	public Applyleave get(int applyleaveId) {
		String sql = "SELECT * FROM applyleave WHERE apply_id=" + applyleaveId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Applyleave>() {

			@Override
			public Applyleave extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Applyleave applyleave = new Applyleave();
					applyleave.setId(rs.getInt("apply_id"));
					applyleave.setApply_email(rs.getString("apply_email"));
					applyleave.setApplyfrom(rs.getString("applyfrom"));
					applyleave.setApplyto(rs.getString("applyto"));
					applyleave.setDays(rs.getInt("days"));
					applyleave.setApplytype(rs.getString("applytype"));
					applyleave.setApplystatus(rs.getString("applystatus"));
					applyleave.setApplytime(rs.getString("applytime"));
					return applyleave;
				}
				
				return null;
			}
			
		});
	}

	@Override
	public void approve(int applyId) {
		String sql = "UPDATE applyleave SET applystatus=?, applytime=CURRENT_TIMESTAMP() "
				+ "WHERE apply_id=?";
	jdbcTemplate.update(sql, "Approved", applyId);

		
	}

}
