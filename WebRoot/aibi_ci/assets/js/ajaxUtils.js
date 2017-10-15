AjaxUtils = function() {

	var AjaxUtils = {
		
		/**
		 * send ajax request with the feedback json data type
		 */
		sendAjax: function(postUrl, postData, callback, block, callbackError) {
			var isBlock = typeof block == 'undefined' ? true : block;
			try {
				if (isBlock) $.blockUI();
			} catch (e) {}
			$.ajax({
				type: "POST",
				url: postUrl, 
			   	cache: false,
			   	dataType: "html",
			   	data: postData, 
			   	success: callback,
			   	error: callbackError || function(){},
			   	complete: function() {
			   		try {
			   			if (isBlock) $.unblockUI();
			   		} catch (e) {}
			   	}
			});
		},
		
		sendAjaxJson: function(postUrl, postData, callback, block, callbackError) {
			var isBlock = typeof block == 'undefined' ? true : block;
			try {
				if (isBlock) $.blockUI();
			} catch (e) {}
			$.ajax({
				type: "POST",
				url: postUrl, 
			   	cache: false,
			   	data: postData, 
			   	dataType: "json",
			   	success: callback,
			   	error: callbackError || function(){},
			   	complete: function() {
			   		try {
			   			if (isBlock) $.unblockUI();
			   		} catch (e) {}
			   	}
			});
		},
		
		ajaxSubmitJson: function(formId, postUrl, callback, callbackError){		
			try {
				$.blockUI();
			} catch (e) {
			}
			$('#'+formId).ajaxSubmit({
				type:"POST",
				url:postUrl,
				dataType:'json',
				success:function(result){		
					callback(result)||function(){};
				},
				error:callbackError || function(){},
				complete: function() {
			   		try {
			   			$.unblockUI();
			   		} catch (e) {}
			   	}
			});
		}
	};
	return AjaxUtils;
}();