<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<link href="${ctx}/aibi_ci/assets/js/jqueryUI/ui-lightness/jquery-ui-1.9.2.custom.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/js/scrollbar/jquery.mCustomScrollbar.css" rel="stylesheet"  />
<link href="${ctx}/aibi_ci/assets/js/multiselect/css/jquery.multiselect.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/js/multiselect/css/jquery.multiselect.filter.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/themes/common.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/tag_main.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/table.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/form.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/panel.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/js/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/default/jquery_ui.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/common.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/themes/default/category.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/main.css" rel="stylesheet"/>
<% if (coc_province.equals("zhejiang")) { //coc_province在include.jsp中定义；该页面需要和include.jsp一起使用，而且放在其之后被引用 %>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/main_zj_small.css" rel="stylesheet"/>
<% } %>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/dialog_overload.css" rel="stylesheet" type="text/css"/>
<c:if test='${cookie[version_flag].value eq "true"}'>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/new_main.css" id="new_main_css" rel="stylesheet"/>
</c:if>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUI/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUI/jquery.blockUI.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUI/jquery.blockUI-css.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/datatable.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/hoverForIE6.js" ></script>
<%-- <script type="text/javascript" src="${ctx}/aibi_ci/assets/js/scrollbar/jquery.mCustomScrollbar.concat.min.js" ></script> --%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/scrollbar/jquery.mCustomScrollbar.js" ></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/select.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/stringUtil.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/aibi_chart/chartswf/FusionCharts.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/multiselect/src/jquery.multiselect.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/multiselect/src/jquery.multiselect.filter.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/document_event.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/accounting.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/main.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common/dialog.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common/requestAnimationFrame.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common/jquery.fly.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common/jQuery.extensions.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/common/jquery.extend.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/searchTip.js"></script>
<script type="text/javascript">
	jQuery.ctx='${ctx}';
	jQuery._maxLabelNum = 20;
	jQuery._maxItemNum = 100;
	jQuery._maxAttrNum = 20;
	jQuery._maxDarkLabelExactValue = 340;
	$.ajaxSetup({"error":myfunc});
	function myfunc(XMLHttpRequest, textStatus, errorThrown){
		if(XMLHttpRequest.status==403){
			window.top.location = "${ctx}/login.jsp";
		}else if(XMLHttpRequest.status==500){
			window.top.location = "${ctx}/login.jsp";
		}else if(XMLHttpRequest.status==408){
			window.top.location = "${ctx}/login.jsp";
		}
	}
	if(!detectFlash()){
		alert("please install flash plugin！"); 
	}
</script>
		