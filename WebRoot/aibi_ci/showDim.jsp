<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>维值选择</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<style type="text/css">
.ztree li span.button.ico_docu{margin-left:0; margin-right:0; margin-top:2px;}
.ztree li span.button.chk{margin-right:1px; margin-top:3px;}
ul.ztree li{line-height:15px;}
ul.ztree li a{height:18px;}
ul.ztree li span.button.switch{height:16px;}
ul.ztree li a:hover{text-decoration:none;}
ul.ztree li a span, ul.ztree li a:hover span{height:17px; line-height:17px;}
ul.ztree li span.button.center_open{background-position:-92px -26px;}
ul.ztree li span.button.center_close{background-position:-74px -26px;}
ul.ztree li span.button.bottom_open{background-position:-92px -47px}
ul.ztree li span.button.bottom_close{background-position:-74px -47px}
ul.ztree li span.button.roots_open{background-position:-92px -3px}
ul.ztree li span.button.roots_close{background-position:-74px -3px}
ul.ztree li span.button.root_open{background-position:-92px -69px}
ul.ztree li span.button.root_close{background-position:-74px -69px}
ul.ztree li a.curSelectedNode{height:18px; overflow:hidden;}
.ztree li.level0 span.level0{display:inline-block; *display:inline; zoom:1;}
</style>
<script type="text/javascript">
var setting = {
	view: {
		selectedMulti: false
	},
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onCheck: onCheck
	}
};

var zNodes;

function onCheck(e, treeId, treeNode) {
	var tar=$("#checkedList");
	var nodeID=treeNode.id;
	var txt=treeNode.name;
	var treeObj=$.fn.zTree.getZTreeObj(treeId);
	if(treeNode.checked){
		var html=$('<li linkid="'+nodeID+'"><a class="close" href="javascript:void(0)">&nbsp;</a>'+txt+'</li>');
		tar.append(html);
		html.find("a").click(function(){
			treeObj.checkNode(treeNode, false, false);
			$(this).parent().remove();
		})
	}else{
		tar.find("li[linkid="+nodeID+"]").remove();
	}
}
var labelId = '${param["labelId"]}';
$(document).ready(function(){
	
	$("#searchDim").click(function(){
		initTree();
		var tar=$("#checkedList");
		var treeObj=$.fn.zTree.getZTreeObj("dataChoose_tree");
		tar.find("li").each(function(){
			var nodeID=$(this).attr("linkid");
			if(treeObj.getNodeByParam("id", nodeID)){
				treeObj.checkNode(treeObj.getNodeByParam("id", nodeID),true);
			}
		});
	});
	
	initTree();
	
	$("#ok").bind("click", function(){
		var tar=$("#checkedList").find("li");
		if(tar.length >0 ){
			submitDims();
		}else{
			parent.showAlert("请选择维值！","failed");
		}
	});
	
	$("#cancel").bind("click", function(){
		parent.closeDimDialog("#dimDialog", "", "");
	});
	
	var attrVal = $(".toSelectDim", parent.document).parent().parent().attr("attrVal");
	if(attrVal != null && attrVal != ""){
		var strs= new Array();
		strs=attrVal.split(",");
		var treeObj=$.fn.zTree.getZTreeObj("dataChoose_tree");
		var tar=$("#checkedList");
		for(var i=0; i<strs.length; i++){
			var node = treeObj.getNodeByParam("id", strs[i]);
			var txt=node.name;
			var nodeID=node.id;
			treeObj.checkNode(node, true, true);
			var html=$('<li linkid="'+nodeID+'"><a class="close" href="javascript:void(0)">&nbsp;</a>'+txt+'</li>');
			tar.append(html);
			html.find("a").click(function(){
				var id=$(this).parent().attr("linkid");
				var treeObj=$.fn.zTree.getZTreeObj("dataChoose_tree");
				var node = treeObj.getNodeByParam("id", id);
				treeObj.checkNode(node, false, false);
				$(this).parent().remove();
			});
		}
	}
	
});

function submitDims(){
	var tar=$("#checkedList");
	var attrVal="";
	var attrValCn="";
	tar.find("li").each(function(i){
		var nodeID=$(this).attr("linkid");
		var name=$.trim($(this).text());
		if(i == 0){
			attrVal = nodeID;
			attrValCn = name;
		}else{
			attrVal += "," + nodeID;
			attrValCn += "," + name;
		}
	});
	parent.closeDimDialog("#dimDialog", attrVal, attrValCn);
}

function initTree(){
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findDimTree.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		data: {
				labelId: labelId,
				dimName: $.trim($("#dimName").val())
			  },
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.dimTree;
			}else{
				alert(result.msg);
			}
			
		}
	});
	$.fn.zTree.init($("#dataChoose_tree"), setting, zNodes);
}
</script>
</head>

<body>
<div class="dataChoose" style="width:750px; margin:20px auto;">
	<div class="aibi_search">
        <ul class="clearfix">
            <li>名称：<input id="dimName" name="dimName" type="text" class="aibi_input" style="width:90px" value=""/></li>
            <!-- <li>编码：<input name="" type="text" class="aibi_input" style="width:90px" value=""/></li> -->
            <li><input id="searchDim" name="searchDim" type="button" class="aibi_search_btn"/></li>
        </ul>
        <div class="clear"></div>
    </div>
    
    <div class="dataChoose_ct">
    	<div class="ct_left">
        	<ul id="dataChoose_tree" class="ztree"></ul>
        </div>
        <dl class="ct_right">
        	<dt>已选数据：</dt>
            <dd>
            	<ul id="checkedList">

                </ul>
            </dd>
        </dl>
    </div>
    
    <div class="dialog_btn_div" style="position:static; border-top:1px solid #b5b5b5; background:none;">
    	<input id="ok" name="" type="button" value="确 定" class="tag_btn"/>
    	<input id="cancel" name="" type="button" value=" 取 消 " class="tag_btn"/>
	</div>
</div>
</body>
</html>
