<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">

		<h1 align="center">User profile form</h1>
		<form:form action="saveUserprofile" method="post" modelAttribute="userprofile">
		<table align="center">
			
			<form:hidden path="id"/>
			<tr>
				<td>Name :</td>
				<td><form:input path="name" /></td>
			</tr>
			<tr>
				<td>Annual balance :</td>
				<td><form:input path="leave_balance_annual" /></td>
			</tr>
			<tr>
				<td>MC balance :</td>
				<td><form:input path="leave_balance_mc" /></td>
			</tr>
			<tr>
				<td>Annual taken :</td>
				<td><form:input path="annual_taken" /></td>
			</tr>
			<tr>
				<td>MC taken :</td>
				<td><form:input path="mc_taken" /></td>
			</tr>
			<tr>
				<td>Role:</td>
			    <td>&nbsp;&nbsp;&nbsp;&nbsp;<form:radiobutton path="role" value="Employee" />Employee&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <form:radiobutton
					path="role" value="Admin" />Admin</td>
			</tr>
			<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="Save changes"></td>
			</tr>
		</table>
		</form:form>

</table>
