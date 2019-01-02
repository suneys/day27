<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>SMTPMS</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/CSS">
		tr{
		  text-align:center; 
		  font-size:40px
		}
		table{
			margin:5px,auto,auto,auto
		}
	
	</style>
  </head>
  
  <body style="margin:0">
  
  	<%
  	response.setIntHeader("Refresh", 1);
  	%>

  	<div style="background-color:#00c8c8">
  		<p style="font-size:30px" align="center">四川长虹精密电子科技有限公司</p>
		<p style="font-size:20px" align="center">(SMTPMS)</p>
	</div>
    <table border="1" align="center" cellspacing="0" width="95%">
    	<tr style="font-size:20px">
    		<th>生产线</th>
    		<th>批次代码</th>
    		<th>计划数量</th>
    		<th>维备件</th>
    		<th>机型</th>
    		<th>A面程序名</th>
			<th>B面程序名</th>
			<th>状态B</th>
			<th>状态A</th>
			<th>当日产量</th>
			<th>累计产量</th>
			<th>定额产量</th>
		</tr>
    	<c:forEach var="en" items="${applicationScope.smtpmsEntities }" varStatus="vs">
    		<tr style="font-size:20px">
    			<td>${en.key }</td>
    			<td>${en.value.getBatchNumber()}</td>
    			<td>${en.value.getPlanned()}</td>
    			<td>${en.value.getRepairableSpares()}</td>
    			<td>${en.value.getMachineNo()}</td>
    			<td>${en.value.getProgramA() }</td>
				<td>${en.value.getProgramB()}</td>
				<td>${en.value.getStatusB()}</td>
				<td>${en.value.getStatusA()}</td>
				<td>${en.value.getOnDayProduction()}</td>
				<td>${en.value.getCumulativeProduction() }</td>
				<td>${en.value.getRatedProduction() }</td>
    		</tr>
    		
    	</c:forEach>
    </table>
  </body>
</html>
