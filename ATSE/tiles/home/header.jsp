<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags" %>

<header>
	<div class="container">
		<div class="row">
			<div class="col-md-7" id="logoPart">
				<div class="state-name" style="cursor: pointer;" onclick="window.location = 'home.action';">Academic Thesaurus Search Engine</div>
			</div>
		</div>
		<br>
	</div>
</header>

<!-- KENDO : Notifications -->
<kendo:notification name="notification" autoHideAfter="3000" stacking="down" >
	<kendo:notification-templates>
		<kendo:notification-template type="error" template="errorTemplate"/>
		<kendo:notification-template type="success" template="successTemplate"/>
		<kendo:notification-template type="info" template="infoTemplate"/>
	</kendo:notification-templates>
</kendo:notification>

<script id="errorTemplate" type="text/x-kendo-template">        
    <div class="wrong">
    	<img src="images/error-icon.png" />
        <h3>Error</h3>
        <p>#= message #</p>
    </div>
</script>
<script id="successTemplate" type="text/x-kendo-template" >
    <div class="success">
    	<img src="images/success-icon.png" />
		<h3>Success</h3>
    	<p>#= message #</p>
    </div>
</script>
<script id="infoTemplate" type="text/x-kendo-template" >
    <div class="info">
		<p style="padding: 10px;">#= message #</p>
		<hr style="margin: 10px 0px;"/>
		<div class="pull-right">
			<button type="button" class='btn btn-primary confirm-yes-btn'>Ok</button>
		</div>
	</div>
</script>

<script id="confirmDeleteTemplate" type="text/x-kendo-template" >
    <p style="padding: 10px;">#= message #</p>
	<hr style="margin: 10px 0px;"/>
	<div class="pull-right">
		<button type="button" class='btn btn-primary confirm-yes-btn'>Yes</button>
		<button type="button" class='btn btn-default confirm-no-btn'>No</button>
	</div>
</script>

<script>
	var notification = $("#notification").kendoNotification({
	    autoHideAfter: 3000,
	    stacking: "down",
	    show: onShow,
	    templates: [{
	        type: "error",
	        template: $("#errorTemplate").html()
	    }, {
	        type: "success",
	        template: $("#successTemplate").html()
	    }, {
	        type: "info",
	        template: $("#infoTemplate").html()
	    }]
	
	}).data("kendoNotification");
	
	
	function onShow(e) {
        if (e.sender.getNotifications().length == 1) {
            var element = e.element.parent(),
                eWidth = element.width(),
                eHeight = element.height(),
                wWidth = $(window).width(),
                wHeight = $(window).height(),
                newTop, newLeft;
            
            newLeft = Math.floor(wWidth - eWidth-50);
            newTop = Math.floor(eHeight-10);

            e.element.parent().css({top: newTop, left: newLeft});
        }
    }
</script>