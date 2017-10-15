$(document).ready(function(){
	var _isSuccess = '${resultMap["success"]}';
	var _msg = '${resultMap["msg"]}';
	if(_isSuccess == 'false'){
		parent.showAlert(_msg,"failed",parent.closePush);
		parent.multiCustomSearch();
	}
	
});

$(function() {
	coreMain.showCustomPushCycle();
	coreMain.changePushRadio(".push_type_radio");
	coreMain.pushCustomerGroupConfirm("#saveBtn");
});