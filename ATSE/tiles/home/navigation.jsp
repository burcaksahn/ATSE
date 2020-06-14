<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %><%@ taglib prefix="s" uri="/struts-tags" %>

<nav class="navbar navbar-expand-md">
	<div class="container">
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar01" aria-controls="navbar01" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"><i class="fa fa-bars" aria-hidden="true"></i></span>
		</button>
		<div class="collapse navbar-collapse" id="navbar01">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item <s:if test="navPageId == 1">active</s:if>">
					<a class="nav-link <s:if test="navPageId == 1">active</s:if>" href="home.action">Home</a>
				</li>
				<li class="nav-item <s:if test="navPageId == 2">active</s:if>">
					<a class="nav-link <s:if test="navPageId == 2">active</s:if>" href="displayclustersearch.action">Cluster Search</a>
				</li>
			</ul>
		</div>
	</div>
</nav>

