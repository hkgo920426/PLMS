<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
    <caption><a href="/newApplyleave">Apply for leave</a></caption>
 	<thead>
		<tr>
				<th>No</th>
	        	<th>From</th>
	        	<th>To</th>
	        	<th>Days</th>
	        	<th>Type</th>
	        	<th>Apply Time</th>
	        	<th>Status</th>
	        	<th>Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="applyleave" items="${listApplyleave}" varStatus="status">
	        	<tr>
	        		<td>${status.index + 1}</td>
					<td>${applyleave.applyfrom}</td>
					<td>${applyleave.applyto}</td>
					<td>${applyleave.days}</td>
					<td>${applyleave.applytype}</td>
					<td>${applyleave.applytime}</td>
					<td>${applyleave.applystatus}</td>
					<td>
						<a href="deleteApplyleave?id=${applyleave.id}">Delete</a>
					</td>
							
	        	</tr>
				</c:forEach>	
	</tbody>
</table>