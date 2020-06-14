<!DOCTYPE html ><%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %><%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %><%@ taglib prefix="s" uri="/struts-tags" %>

<tiles:importAttribute name="title" scope="request"/>
<html lang="en">
    <head>
		<title>ATSE - <tiles:getAsString name="title"/></title>
   		<tiles:insertAttribute name="head"/>
    </head>
	<body>
	    <tiles:insertAttribute name="header"/>
	    <tiles:insertAttribute name="navigation"/>
	    <!--main content-->
		<div class="container" style="min-height:630px; margin-bottom:20px;">
			<tiles:insertAttribute name="body" />
		</div>
		<tiles:insertAttribute name="footer" />
	</body>
</html>
