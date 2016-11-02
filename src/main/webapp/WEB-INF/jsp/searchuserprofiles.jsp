<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
    <caption>List of searched users</caption>
 	<thead>
		<tr>
				<th>No</th>
				<th>Role</th>
	        	<th>Name</th>
	        	<th>Email Address</th>
	        	<th>Annual Balance</th>
	        	<th>MC Balance</th>
	        	<th>Annual taken</th>
	        	<th>MC taken</th>
	        	<th>Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="userprofile" items="${listUserprofile}" varStatus="status">
	        	<tr>
	        		<td>${status.index + 1}</td>
					<td>${userprofile.role}</td>
					<td>${userprofile.name}</td>
					<td>${userprofile.email_address}</td>
					<td>${userprofile.leave_balance_annual}</td>
					<td>${userprofile.leave_balance_mc}</td>
					<td>${userprofile.annual_taken}</td>
					<td>${userprofile.mc_taken}</td>
					<td>
						<a href="editUserprofile?id=${userprofile.id}">Edit</a>
					</td>
							
	        	</tr>
				</c:forEach>	
	</tbody>
</table>