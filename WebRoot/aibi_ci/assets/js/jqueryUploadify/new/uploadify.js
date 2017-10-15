$(function(){
	var attachmentSpanId = 0;
	$("#file_upload_dark").uploadify({
		buttonClass	  : "uploadBtn", 
		btnBoxClass   : "update-file-btn",
		buttonText	  : "选择文件",
		width		  : 60,
		height		  : 24,
		fileObjName	  : "files",
		auto		  : true,//
		multi		  : true,
		//限制在一次队列中的次数（可选定的文件个数）
		queueSizeLimit: 5, 
		progressData  : "percentage",
		itemTemplate : '<div class="uploadify-content fl" id="${fileID}"><div class="uploadify-loading" ><div class="fileName">'
			+' <span class="file-Name" title="${fileName}(${fileSize})">${fileName}(${fileSize})</span><span class="data"></span></div></div></div>',
		fileTypeExts  : "*.jpg;*.png;*.gif;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpeg;*.rar;*.zip;*.pdf;*.txt;*.csv",
		fileTypeDesc  : "只能上传.jpg;.png;.jpeg;.gif;.doc;.docx;.xls;.xlsx;.ppt;.pptx;.pdf;.txt;.csv;.rar;.zip格式的文件",
        swf			  : $.ctx + "/aibi_ci/assets/js/jqueryUploadify/uploadify.swf",
        uploader	  : $.ctx + "/ci/feedback/ciFeedbackAction!saveAttenchments.ai2do",
        sizeLimit     : 5120000,
        //能同时上传的文件数目
        uploadLimit   : 5,
        overrideEvents : ["onUploadError","onSelectError","onDialogClose"],
        //返回一个错误，选择文件的时候触发
        onSelectError : function(file){
        	commonUtil.create_alert_dialog("showUploadAlertDialog", {
    			"txt":"请注意，上传附件不能超出5个！",
    			"type":"failed",
    			"width":500,
    			"height":200
    		});
    	},
        onSelect : function(file) {
        	$(".uploadify-loading").parent().show();
        	$("#file_upload_dark-queue").show();
        },
        onCancel : function(event, queueId, fileObj, data){
        },
        onSelectOnce : function(event, data){
        },
        onUploadSuccess : function(file, data, response) { 
        	var success = $.parseJSON(data).success;
        	if(!success) {
        		return;
        	}
        	$(".uploadify-loading").parent().hide(2000);
        	var content = $("<div attachmentSpanId='"+attachmentSpanId+"' class='uploadify-success-list'><div class='file-name'>"+file.name+"("+file.size+")</div><div class='del-item hidden' onclick='delAttachment(this,"+attachmentSpanId+",\""+file.id+"\");'>x</div></div>").css("background","url("+$.ctx+"/aibi_ci/assets/js/jqueryUploadify/file_win.png) no-repeat center center");
        	$(".upload_success").append(content); 
        	$("#file_upload_dark-queue").hide();
        	
        	var dataObj = $.parseJSON(data).attachment;
        	var html="";
			html+="<span id='"+attachmentSpanId+"'fileName='"+dataObj.fileName+"' fileOldName='"+dataObj.fileOldName+"' fileType='"+dataObj.fileType+"' fileUrl='"+dataObj.fileUrl+"'></span>";      	
			attachmentSpanId++;
			$("#attachmentList").append(html);
        },
        onUploadError :  function(){
        	commonUtil.create_alert_dialog("showUploadAlertDialog", {
    			"txt":"请注意，上传附件不能超出5个！",
    			"type":"failed",
    			"width":500,
    			"height":200
    		});
        }
	});
	
});