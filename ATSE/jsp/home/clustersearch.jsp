<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags" %>

<div class="container" style="min-height:580px;">
	<h3>Cluster Search</h3> 
	<br/>
	
	<div class="row">
		<div class="col">
			<label for="searchWord" style="font-size: larger;font-weight: bold;">Search Word</label>
			<input type="text" class="form-control" id="searchWord" required/>
		</div>
	</div>
	<div class="row mt-2">
		<div class="col">
			<button class="btn btn-primart" onclick="searchCluster()">Search</button>
		</div>
	</div>
	<br/>
	<kendo:grid name="clusterSearchTable" selectable="true" columnMenu="true" filterable="true" groupable="false" pageable="true" columnReorder="true" sortable="true" height="500px" resizable="true">
		<kendo:grid-pageable refresh="true" />
		<kendo:grid-sortable allowUnsort="false" mode="single" />
		<kendo:grid-columns>
			<kendo:grid-column title="Index" filterable="true" field="index" width="50px"/>
			<kendo:grid-column title="Cluster No" field="clusterNo" width="70px"/>
			<kendo:grid-column title="Word List" field="wordList" width="400px"/>
		</kendo:grid-columns>
		<kendo:dataSource pageSize="10000">
			<kendo:dataSource-transport>
				<kendo:dataSource-transport-read url="searchclusterlistjson.action" dataType="json" type="POST" contentType="application/json" />
			</kendo:dataSource-transport>
			<kendo:dataSource-schema>
				<kendo:dataSource-schema-model id="index">
					<kendo:dataSource-schema-model-fields >						
						<kendo:dataSource-schema-model-field name="index" type="number" editable="false"/>
						<kendo:dataSource-schema-model-field name="wordList" type="string" />
						<kendo:dataSource-schema-model-field name="clusterNo" type="number"/>
					</kendo:dataSource-schema-model-fields>
				</kendo:dataSource-schema-model>
	    		</kendo:dataSource-schema>
	    </kendo:dataSource>
	</kendo:grid>
	
	<br/><br/>
</div>

<script>

	$(document).ready(function() {
		
	});
	
	function searchCluster(){
		var searchWord=$("#searchWord").val();
		if(searchWord!=""){
			$("#clusterSearchTable").data("kendoGrid").dataSource.transport.options.read.url="searchclusterlistjson.action?searchWord="+searchWord;
			$("#clusterSearchTable").data("kendoGrid").dataSource.read();
		}
		else{
    		notification.show({message: "Please enter a search word!"}, "error");
		}
	}
</script>