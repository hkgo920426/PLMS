<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:if test="${error ne null}">
	<div class="alert alert-danger">${error}</div>
</c:if>
<div class="jumbotron">
<h1>User Profile</h1>
<c:if test="${adminConnected}">
	<form method="post" action="searchuserprofiles">
	<table class='table'>
   		<tr>
   			<td>Look for other users</td>
   			<td><input type="text" name="search_email" size="60" /></td>
   			<td><input type="submit" value="Search" /></td>
   		</tr>
     </table>
  </form>
 </c:if>
	
	<table class="table">
	<tr>
		<th>Role</th><th>${userprofile.role}</th>
	</tr>
	<tr>
		<th>Name</th><th>${userprofile.name}</th>
	</tr>
	<tr>
		<th>Email Address</th><th>${userprofile.email_address}</th>
	</tr>
	<tr>
		<th>Annual leave balance</th><th>${userprofile.leave_balance_annual}</th>
	</tr>
	<tr>
		<th>MC leave balance</th><th>${userprofile.leave_balance_mc}</th>
	</tr>
	<tr>
		<th>Annual leave taken</th><th>${userprofile.annual_taken}</th>
	</tr>
	<tr>
		<th>MC taken</th><th>${userprofile.mc_taken}</th>
	</tr>
	
	</table>
	
	</div>