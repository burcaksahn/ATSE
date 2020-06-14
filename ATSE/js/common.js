function goTo(url){
	window.location.href = url;
}


//Convert To CheckBox
function convertToCheckbox(cbValue){
	var returnValue = "";
	
	var cbChecked = "";
		
	if (cbValue)
		cbChecked = "checked='checked'";

	returnValue = "<input type='checkbox' disabled='disabled' " + cbChecked + " />";
	
	return returnValue;
}

//Get Table Item Count
function getTableCount(gridName) {
	var gridData = $("#" + gridName).data("kendoGrid");
	return gridData.dataSource.total();
}

function eqFormPage()
{
	var tHeight = 0;
	
	if (document.getElementById('treeBody') != null)
		tHeight = document.getElementById('treeBody').offsetHeight;
	if (tHeight != 0){
		
		var fHeight = document.getElementById('formBody').offsetHeight;
	//	var sHeight = document.getElementById('submitForm').offsetHeight;
		var qHeight = document.getElementById('questions').offsetHeight;
		
		var sHeight = 102;
		var minHeight = 450;
		var margin = 126;
		
	//	alert(tHeight)
	//	alert(fHeight);
	//	alert(sHeight);
		
		var nMax = Math.max(tHeight, fHeight+sHeight);
	
	//	alert(nMax);
		
	//	alert(document.getElementById('questions'));
		
	//	document.getElementById('treeBody').style.height=nMax+sHeight+"px";
	//	document.getElementById('formBody').style.height=nMax+"px";
	//	document.getElementById('questions').style.height=(nMax)+"px";
		
	//	alert("form height: "+fHeight);
	//	alert("questions height: "+qHeight);
	//	alert("tree height: "+tHeight);
	
		if (nMax < minHeight){
	//		alert('1');
			document.getElementById('treeBody').style.height = minHeight+"px";
			document.getElementById('questions').style.height = (minHeight-sHeight)+"px";
			
		}else{
			if (fHeight+sHeight > tHeight){
	//			alert('2');
				document.getElementById('treeBody').style.height = (fHeight+sHeight)+"px";
			}else{
	//			alert('3');
				document.getElementById('questions').style.height = (tHeight-sHeight) +"px";
			}
		}
	}
	if (document.getElementById('submitForm') != null)
		document.getElementById('submitForm').style.display = 'block';
}