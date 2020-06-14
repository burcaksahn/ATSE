<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags" %>


<div class="container" style="min-height:580px;">
	<h3>Cluster Management</h3> 
	<br/>
	<form method="post" action="/kendo-ui/upload/submit">
         <div class="demo-section k-content">
             <input name="wordListFile" id="wordListFile" type="file" aria-label="wordListFile" />
         </div>
     </form>
     
     <br/>
	
	<kendo:grid name="clustersGrid" editable="true" selectable="true" columnMenu="true" filterable="true" groupable="false" pageable="true" columnReorder="true" sortable="true" height="500px" resizable="true">
		<kendo:grid-pageable refresh="true" />
		<kendo:grid-sortable allowUnsort="false" mode="single" />
		<kendo:grid-columns>
			<kendo:grid-column title="Index" filterable="true" field="index" width="50px"/>
			<kendo:grid-column title="Cluster No" field="clusterNo" width="70px"/>
			<kendo:grid-column title="Word List" field="wordList" width="400px"/>
		</kendo:grid-columns>
		<kendo:dataSource pageSize="10000">
			<kendo:dataSource-transport>
				<kendo:dataSource-transport-read url="getclusterlistjson.action" dataType="json" type="POST" contentType="application/json" />
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
        $("#wordListFile").kendoUpload({
            async: {
            },
           	select:onSelect
        });
	});
	function onSelect(e){
		var data;
	    data = new FormData();
	    data.append( 'wordListFile', e.files[0].rawFile );
	    var form = $('form')[0]; 
	    var formData = new FormData(form);
		
		$.ajax({
			  type: "POST",
			  url: "uploadfile.action",
			  data: formData,
			  contentType: false,
		      processData: false,
			  success: function(data){
				notification.show({message: "Clusters are loaded successfully."}, "success");
				$("#clustersGrid").data("kendoGrid").dataSource.read();
			  },
			  error: function(err){
				notification.show({message: "An error occured."}, "error");
			  }
			});
	}
</script>
<style>
	.k-edit-form-container{
		width: 500px;
	}
	.k-widget.k-window {
		width: 500px;
	}
	
	input[type="file"] {
	    display: block;
	}
</style>