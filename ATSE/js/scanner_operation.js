/// <reference path="../Resources/dynamsoft.webtwain.intellisense.js" />
//--------------------------------------------------------------------------------------
//************************** Import Image*****************************
//--------------------------------------------------------------------------------------
/*-----------------select source---------------------*/
function source_onchange() {
	if (document.getElementById("divTwainType"))
		document.getElementById("divTwainType").style.display = "";
	if (document.getElementById("btnScan"))
		document.getElementById("btnScan").value = "Scan";

	if (document.getElementById("source"))
		DWObject.SelectSourceByIndex(document.getElementById("source").selectedIndex);
}


/*-----------------Acquire Image---------------------*/
function acquireImage() {
	DWObject.SelectSourceByIndex(document.getElementById("source").selectedIndex);
	DWObject.CloseSource();
	DWObject.OpenSource();
	DWObject.IfShowUI = false; //document.getElementById("ShowUI").checked;

	var i;
	for (i = 0; i < 3; i++) {
		if (document.getElementsByName("PixelType").item(i).checked == true)
			DWObject.PixelType = i;
	}
	
	DWObject.Resolution = document.getElementById("Resolution").value;
	DWObject.IfFeederEnabled = document.getElementById("ADF").checked;
	DWObject.IfDuplexEnabled = document.getElementById("Duplex").checked;
	if (Dynamsoft.Lib.env.bWin || (!Dynamsoft.Lib.env.bWin && DWObject.ImageCaptureDriverType == 0))
		appendMessage("Pixel Type: " + DWObject.PixelType + "<br />Resolution: " + DWObject.Resolution + "<br />");

	DWObject.IfDisableSourceAfterAcquire = true;
	DWObject.AcquireImage();
}

/*-----------------Load Image---------------------*/
function btnLoad_onclick() {
	var OnSuccess = function() {
		appendMessage("Loaded an image successfully.<br/>");
		updatePageInfo();
	};

	var OnFailure = function(errorCode, errorString) {
		checkErrorStringWithErrorCode(errorCode, errorString);
	};
	
	DWObject.IfShowFileDialog = true;
	DWObject.LoadImageEx("", EnumDWT_ImageType.IT_ALL, OnSuccess, OnFailure);
}

//--------------------------------------------------------------------------------------
//************************** Edit Image ******************************

//--------------------------------------------------------------------------------------
function btnShowImageEditor_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.ShowImageEditor();
}

function btnRotateRight_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.RotateRight(DWObject.CurrentImageIndexInBuffer);
	appendMessage('<b>Rotate right: </b>');
	if (checkErrorString()) {
		return;
	}
}
function btnRotateLeft_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.RotateLeft(DWObject.CurrentImageIndexInBuffer);
	appendMessage('<b>Rotate left: </b>');
	if (checkErrorString()) {
		return;
	}
}

function btnRotate180_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.Rotate(DWObject.CurrentImageIndexInBuffer, 180, true);
	appendMessage('<b>Rotate 180: </b>');
	if (checkErrorString()) {
		return;
	}
}

function btnMirror_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.Mirror(DWObject.CurrentImageIndexInBuffer);
	appendMessage('<b>Mirror: </b>');
	if (checkErrorString()) {
		return;
	}
}
function btnFlip_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.Flip(DWObject.CurrentImageIndexInBuffer);
	appendMessage('<b>Flip: </b>');
	if (checkErrorString()) {
		return;
	}
}

//--------------------------------------------------------------------------------------
//************************** Upload Image***********************************
//--------------------------------------------------------------------------------------
var ProcssedImagesCount = 0;
var imageArrays = [];
var totalCount = 0;
function btnUpload_onclick() {
	
	var index = DWObject.HowManyImagesInBuffer;
	var barCodeArr = [];
	
	for (var k = 0; k < index; k++){
		barCodeArr[k] = ReadBarcode(k);
		if(parseInt(barCodeArr[k]) > 2){
			var multiBarcode = barCodeArr[k] +","+(k+1)+"|";
			var readedBarcode = $('#multiLetterBarcode').val();
    		$('#multiLetterBarcode').val(readedBarcode+multiBarcode);
		}
		console.log("page: "+k+" barcode: "+barCodeArr[k]);
	}
	
	console.log("Done reading barcodes");
	
	$("#saveButton").html("Saving Documents....");
	
	if (!checkIfImagesInBuffer()) {
		return;
	}
	
	var i, strHTTPServer, strActionPage, strImageType;
  
	//DWObject.MaxInternetTransferThreads = 5;
	strHTTPServer = location.hostname;
	
//	Dynamlib is not defined // ERROR
//	DWObject.IfSSL = DynamLib.detect.ssl;
	DWObject.IfSSL = true;
	var _strPort = location.port == "" ? 80 : location.port;
//	if (DynamLib.detect.ssl == true)
	if (true)
		_strPort = location.port == "" ? 443 : location.port;
	DWObject.HTTPPort = _strPort;
	

	var CurrentPathName = unescape(location.pathname); // get current PathName in plain ASCII	
	var CurrentPath = CurrentPathName.substring(0, CurrentPathName.lastIndexOf("/") + 1);
	strActionPage = CurrentPath + "document.save"; //the ActionPage's file path , Online Demo:"SaveToDB.aspx" ;Sample: "SaveToFile.aspx";
	
	var guid = document.getElementById("guid").value;
	
	// Set Image Type As Pdf
	strImageType = 4;

	// Set additional parameters
	var guid = document.getElementById("guid").value;
	var letterId = document.getElementById("letterId").value;
	
	var uploadfilename = guid + document.getElementsByName("ImageType").item(i).value;

	var OnSuccess = function(httpResponse) {
		appendMessage('<b>Upload: </b>');
		checkErrorStringWithErrorCode(0, "Successful.");
		//window.location.href = redirectURLifOK;
		var readedBarcode = $('#multiLetterBarcode').val();
		console.log("multicode&"+readedBarcode)
		parent.postMessage("multicode&"+readedBarcode, "*" );
		parent.postMessage("scan-complete", "*");
	};

	var OnFailure = function(errorCode, errorString, httpResponse) {
		checkErrorStringWithErrorCode(errorCode, errorString, httpResponse);
	};
	
	DWObject.SetHTTPFormField('guid', guid);
	DWObject.SetHTTPFormField('letterId', letterId);
	
	var CurrentPathName = unescape(location.pathname); // get current PathName in plain ASCII	
    var CurrentPath = CurrentPathName.substring(0, CurrentPathName.lastIndexOf("/") + 1);
    strActionPage = CurrentPath + "document.save"; //the ActionPage's file path , Online Demo:"SaveToDB.aspx" ;Sample: "SaveToFile.aspx";
    var redirectURLifOK = unescape(location.host) + CurrentPath + "savecomplete.action?guid="+guid;
    
    console.log("Redirect URL : " + redirectURLifOK);
    
	if (document.getElementById("MultiPagePDF").checked) {
		if ((DWObject.SelectedImagesCount == 1) || (DWObject.SelectedImagesCount == DWObject.HowManyImagesInBuffer)) {
				DWObject.HTTPUploadAllThroughPostAsPDF(
					strHTTPServer,
					strActionPage,
					uploadfilename,
					OnSuccess, OnFailure
				);
			}
			else {
				DWObject.HTTPUploadThroughPostAsMultiPagePDF(
					strHTTPServer,
					strActionPage,
					uploadfilename,
					OnSuccess, OnFailure
				);
			}
	}else{
			DWObject.HTTPUploadAllThroughPostAsPDF(
				strHTTPServer,
				strActionPage,
				uploadfilename,
				OnSuccess, OnFailure
			);
		}
	}

//--------------------------------------------------------------------------------------
//************************** BarCode functions***********************************
//--------------------------------------------------------------------------------------

function GetBarcodeInfo(sImageIndex, result) {//This is the function called when barcode is read successfully
    //Retrieve barcode details
    var count = result.GetCount();
    if (count == 0) {
    	alert("The barcode for the selected format is not found.");
        return;
    } else {
       
       var multiBarcode = "";
       for (i = 0; i < count; i++) {
    	    console.log(result)
    	    var text = result.GetContent(i);
            var x = result.GetX1(i);
            var y = result.GetY1(i);
            var format = result.GetFormat(i);
            var barcodeText = ("barcode[" + (i + 1) + "]: " + text + "\n");
            barcodeText += ("format:" + (format == 4 ? "Code 39" : "Code 128") + "\n");
            barcodeText += ("x: " + x + " y:" + y + "\n");
            var strBarcodeString = text + "\r\n" + (format == 4 ? "Code 39" : "Code 128");
            DWObject.AddText(DWObject.CurrentImageIndexInBuffer, x, y, strBarcodeString, -1, 94700, 0, 1);
            if(parseInt(text) > 2)
            	multiBarcode += text +",2|";
            console.log("Barcode No : " + i + " code : " + text + " Detail : "+ barcodeText);
        }
    	
    	// Get First Barcode
    	$('#letterId').val(result.GetContent(0));
    	var readedBarcode = $('#multiLetterBarcode').val();
    	$('#multiLetterBarcode').val(readedBarcode+multiBarcode);
    	
    }
}

function GetErrorInfo(errorcode, errorstring) {//This is the function called when barcode reading fails
    alert(errorstring);
}

function ReadBarcode(index) {
	
	if(index == DWObject.HowManyImagesInBuffer) 
		return;
	
	var barcodeAddonPath = "";
	if(Dynamsoft.Lib.env.bMac){
		barcodeAddonPath = "../resources/addon/MacBarcode.zip";
	}else{
		barcodeAddonPath = "../resources/addon/Barcode.zip"
	}
	
	DWObject.Addon.Barcode.Download(barcodeAddonPath);
	
	var result = DWObject.Addon.Barcode.Read(index, EnumDWT_BarcodeFormat.CODE_39);
	
	ProcssedImagesCount++;
	
	var count = result.GetCount();
					
	if (count == 0) {
		console.log("No barcode found on image " + index);
	} else {
		
		console.log("Barcode found on image " + index);
		var multiBarcode = "";
		var barcodeText = result.GetContent(0);
		
		if((barcodeText == "1" || barcodeText == "2") && result.GetCount() > 1)
			barcodeText = result.GetContent(1);
		
		console.log("Barcode text " + 0 + ": " + barcodeText);
		
		return barcodeText;
		
	}
	
	console.log("returning for: "+totalCount);
	
	return totalCount;
}

/*function ReadBarcode() {
	
	function readBarcodeInner() {
        //Get barcode result.
        result = DWObject.Addon.Barcode.Read(DWObject.CurrentImageIndexInBuffer, EnumDWT_BarcodeFormat.CODE_39, GetBarcodeInfo, GetErrorInfo);
    }
	if (DWObject) {
        if (DWObject.Addon.Barcode.GetLocalVersion() == "9, 6, 0, 116") {
            if (DWObject.HowManyImagesInBuffer == 0) {
                alert("Please scan or load an image first.");
                return;
            }
            readBarcodeInner();
        }
        else {
        	DWObject.Addon.Barcode.Download("../resources/addon/Barcode.zip", readBarcodeInner, function (errorCode, errorString) { alert(errorString); });
        }
    }
}*/

//--------------------------------------------------------------------------------------
//************************** Navigator functions***********************************
//--------------------------------------------------------------------------------------

function btnFirstImage_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.CurrentImageIndexInBuffer = 0;
	updatePageInfo();
}

function btnPreImage_wheel() {
	if (DWObject.HowManyImagesInBuffer != 0)
		btnPreImage_onclick();
}

function btnNextImage_wheel() {
	if (DWObject.HowManyImagesInBuffer != 0)
		btnNextImage_onclick();
}

function btnPreImage_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	else if (DWObject.CurrentImageIndexInBuffer == 0) {
		return;
	}
	DWObject.CurrentImageIndexInBuffer = DWObject.CurrentImageIndexInBuffer - 1;
	updatePageInfo();
}
function btnNextImage_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	else if (DWObject.CurrentImageIndexInBuffer == DWObject.HowManyImagesInBuffer - 1) {
		return;
	}
	DWObject.CurrentImageIndexInBuffer = DWObject.CurrentImageIndexInBuffer + 1;
	updatePageInfo();
}


function btnLastImage_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.CurrentImageIndexInBuffer = DWObject.HowManyImagesInBuffer - 1;
	updatePageInfo();
}

function btnRemoveCurrentImage_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.RemoveAllSelectedImages();
	if (DWObject.HowManyImagesInBuffer == 0) {
		document.getElementById("DW_TotalImage").value = DWObject.HowManyImagesInBuffer;
		document.getElementById("DW_CurrentImage").value = "";
		return;
	}
	else {
		updatePageInfo();
	}
}


function btnRemoveAllImages_onclick() {
	if (!checkIfImagesInBuffer()) {
		return;
	}
	DWObject.RemoveAllImages();
	document.getElementById("DW_TotalImage").value = "0";
	document.getElementById("DW_CurrentImage").value = "";
}
function setlPreviewMode() {
	var varNum = parseInt(document.getElementById("DW_PreviewMode").selectedIndex + 1);
	var btnCrop = document.getElementById("btnCrop");
	if (btnCrop) {
		var tmpstr = btnCrop.src;
		if (varNum > 1) {
			tmpstr = tmpstr.replace('Crop.', 'Crop_gray.');
			btnCrop.src = tmpstr;
			btnCrop.onclick = function() { };
		}
		else {
			tmpstr = tmpstr.replace('Crop_gray.', 'Crop.');
			btnCrop.src = tmpstr;
			btnCrop.onclick = function() { btnCrop_onclick(); };
		}
	}

	DWObject.SetViewMode(varNum, varNum);
	if (Dynamsoft.Lib.env.bMac) {
		return;
	}
	else if (document.getElementById("DW_PreviewMode").selectedIndex != 0) {
		DWObject.MouseShape = true;
	}
	else {
		DWObject.MouseShape = false;
	}
}

//--------------------------------------------------------------------------------------
//*********************************radio response***************************************
//--------------------------------------------------------------------------------------
function rdTIFFsave_onclick() {
	var _chkMultiPageTIFF_save = document.getElementById("MultiPageTIFF_save");
	_chkMultiPageTIFF_save.disabled = false;
	_chkMultiPageTIFF_save.checked = false;

	var _chkMultiPagePDF_save = document.getElementById("MultiPagePDF_save");
	_chkMultiPagePDF_save.checked = false;
	_chkMultiPagePDF_save.disabled = true;
}
function rdPDFsave_onclick() {
	var _chkMultiPageTIFF_save = document.getElementById("MultiPageTIFF_save");
	_chkMultiPageTIFF_save.checked = false;
	_chkMultiPageTIFF_save.disabled = true;

	var _chkMultiPagePDF_save = document.getElementById("MultiPagePDF_save");
	_chkMultiPagePDF_save.disabled = false;
	_chkMultiPagePDF_save.checked = false;
}
function rdsave_onclick() {
	var _chkMultiPageTIFF_save = document.getElementById("MultiPageTIFF_save");
	_chkMultiPageTIFF_save.checked = false;
	_chkMultiPageTIFF_save.disabled = true;

	var _chkMultiPagePDF_save = document.getElementById("MultiPagePDF_save");
	_chkMultiPagePDF_save.checked = false;
	_chkMultiPagePDF_save.disabled = true;
}
function rdTIFF_onclick() {
	var _chkMultiPageTIFF = document.getElementById("MultiPageTIFF");
	_chkMultiPageTIFF.disabled = false;
	_chkMultiPageTIFF.checked = false;

	var _chkMultiPagePDF = document.getElementById("MultiPagePDF");
	_chkMultiPagePDF.checked = false;
	_chkMultiPagePDF.disabled = true;
}
function rdPDF_onclick() {
	var _chkMultiPageTIFF = document.getElementById("MultiPageTIFF");
	_chkMultiPageTIFF.checked = false;
	_chkMultiPageTIFF.disabled = true;
	
	var _chkMultiPagePDF = document.getElementById("MultiPagePDF");
	_chkMultiPagePDF.disabled = false;
	_chkMultiPagePDF.checked = false;

}
function rd_onclick() {
	var _chkMultiPageTIFF = document.getElementById("MultiPageTIFF");
	_chkMultiPageTIFF.checked = false;
	_chkMultiPageTIFF.disabled = true;
	
	var _chkMultiPagePDF = document.getElementById("MultiPagePDF");
	_chkMultiPagePDF.checked = false;
	_chkMultiPagePDF.disabled = true;
}



//--------------------------------------------------------------------------------------
//************************** Dynamic Web TWAIN Events***********************************
//--------------------------------------------------------------------------------------

function Dynamsoft_OnPostTransfer() {
	updatePageInfo();
}

function Dynamsoft_OnPostLoadfunction(path, name, type) {
	updatePageInfo();
}

function Dynamsoft_OnPostAllTransfers() {
	updatePageInfo();
	checkErrorString();
}

function Dynamsoft_OnMouseClick(index) {
	updatePageInfo();
}

function Dynamsoft_OnMouseRightClick(index) {
	// To add
}


function Dynamsoft_OnImageAreaSelected(index, left, top, right, bottom) {
	_iLeft = left;
	_iTop = top;
	_iRight = right;
	_iBottom = bottom;
}

function Dynamsoft_OnImageAreaDeselected(index) {
	_iLeft = 0;
	_iTop = 0;
	_iRight = 0;
	_iBottom = 0;
}

function Dynamsoft_OnMouseDoubleClick() {
	return;
}


function Dynamsoft_OnTopImageInTheViewChanged(index) {
	_iLeft = 0;
	_iTop = 0;
	_iRight = 0;
	_iBottom = 0;
	DWObject.CurrentImageIndexInBuffer = index;
	updatePageInfo();
}

function Dynamsoft_OnGetFilePath(bSave, count, index, path, name) {
}
