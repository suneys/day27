<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>列表</title>
    
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
  		<p style="font-size:50px" align="center">四川长虹精密电子科技有限公司</p>
		<p style="font-size:40px" align="center">(SMDRS)</p>
	</div>
    <table border="1" align="center" cellspacing="0" width="95%">
    	<tr style="font-size:40px">
    		<th>生产线</th>
    		<th>程序名</th>
    		<th>批次号</th>
    		<th>已更换</th>
    		<th>待确定</th>
    		<th>已确定</th>   		
    	</tr>
    	<c:forEach var="en" items="${applicationScope.lineEntities }" varStatus="vs">
    		<tr>
    			<td>${en.key }</td>
    			<td>${en.value.getProgramName()}</td>
    			<td>${en.value.getBathNumber()}</td>
    			<td>${en.value.getTotalCount()}</td>
    			<td>${en.value.getTotalCount()- en.value.getOkCount()}</td>
    			<td>${en.value.getOkCount() }</td>
    		</tr>
    		
    	</c:forEach>
    </table>
  </body>
</html>
