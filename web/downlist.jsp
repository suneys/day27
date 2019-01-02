<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>下载列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <table border="1" align="center" cellpadding="0">
    	<tr>
    		<th>序号</th>
    		<th>文件名</th>
    		<th>操作</th>
    	</tr>
    	<c:forEach var="en" items="${requestScope.fileNames }" varStatus="vs">
    		<tr>
    			<td>${vs.count }</td>
    			<td>${en.value }</td>
    			<td>
    				<c:url var="url" value="FileServlet">
 						<c:param name="method" value="down"></c:param>   				
 						<c:param name="fileName" value="${en.key }"></c:param>   				
    				</c:url>
    				<a href="${url }">下载</a>
    			</td>
    		</tr>
    		
    	</c:forEach>
    </table>
  </body>
</html>
