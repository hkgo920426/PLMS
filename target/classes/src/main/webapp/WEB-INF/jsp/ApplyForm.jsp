<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    var dateFormat = "mm/dd/yy",
      from = $( "#from" )
        .datepicker({
        	beforeShowDay: $.datepicker.noWeekends,
        	defaultDate: "+1w",
          changeMonth: true,
          numberOfMonths: 1
        })
        .on( "change", function() {
          to.datepicker( "option", "minDate", getDate( this ) );
        }),
      to = $( "#to" ).datepicker({
    	  beforeShowDay: $.datepicker.noWeekends,
    	  defaultDate: "+1w",
        changeMonth: true,
        numberOfMonths: 1
      })
      .on( "change", function() {
        from.datepicker( "option", "maxDate", getDate( this ) );
      });
 
    function getDate( element ) {
      var date;
      try {
        date = $.datepicker.parseDate( dateFormat, element.value );
      } catch( error ) {
        date = null;
      }
 
      return date;
    }
    
   
    
  } );
  </script>


		<h1 align="center">Leave application form</h1>
		<form:form action="saveApplyleave" method="post" modelAttribute="applyleave">
		<table align="center">
			
			<form:hidden path="id"/>
			<tr>
				<td>From:</td>
				<td><form:input id="from" path="applyfrom" /></td>
			</tr>
			<tr>
				<td>To:</td>
				<td><form:input id="to" path="applyto" /></td>
			</tr>
			<tr>
				<td>Type:</td>
			    <td>&nbsp;&nbsp;&nbsp;&nbsp;<form:radiobutton path="applytype" value="Annual" />Annual&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <form:radiobutton
					path="applytype" value="MC" />MC</td>
			</tr>
			<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="Submit Your application"></td>
			</tr>
		</table>
		</form:form>

</table>
