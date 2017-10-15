<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/js/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
<title>帮助中心</title>
<script type="text/javascript">
$(function(){
})
function show_clipboard_table(ts){
	var tar=$("#clipboard_table");
	$(ts).hasClass("showed")?tar.slideUp("fast",function(){$(ts).removeClass("showed")}):tar.slideDown("fast",function(){$(ts).addClass("showed")});
	$(ts).blur();
}

function to_down(fileName) {
	var param = '';
	param = param + 'id='+encodeURI(encodeURI(fileName));
	var action_url = $.ctx+'/ci/ciIndexAction!helpDown.ai2do?'+param;

	window.open(action_url);
}
</script>
</head>
<body>
<div class="newHeader">
	<div class="header_left" style="background:url(${ctx}/aibi_ci/assets/themes/default/images/header_left_<%=teleOperator %>.png) no-repeat;">
        <img src="${ctx}/aibi_ci/assets/themes/default/images/title_<%=coc_province %>.png" /></div>
	<div class="clear"></div>
</div>
<div class="customerTop">
    <dl class="helpcenter">
		<dd><!--<div class="fright">客服电话：内部分机：6626<p>外线：13813813888</p></div>-->帮助中心</dd>
	</dl>
    <div class="clear"></div>
</div>

<div class="help_ct notice">
	<dl>
        <dd>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="noc_l"><span class="video_down"></span><p>功能指导视频下载</p></td>
                    <td>
                        <ul class="help_list">
                        	<li onclick="to_down('bqdbfx.wmv');">
                            	<div class="video_down_lit">&nbsp;</div>
                                <p>标签对比分析</p>
                            </li>
                            <li onclick="to_down('bqfxjk.wmv');">
                            	<div class="video_down_lit">&nbsp;</div>
                                <p>标签分析监控</p>
                            </li>
                            <li onclick="to_down('bqglfx.wmv');">
                            	<div class="video_down_lit">&nbsp;</div>
                                <p>标签关联分析</p>
                            </li>
                            <li onclick="to_down('khqglfx.wmv');">
                            	<div class="video_down_lit">&nbsp;</div>
                                <p>客户群关联分析</p>
                            </li>
                            <li onclick="to_down('khqzbwf.wmv');">
                            	<div class="video_down_lit">&nbsp;</div>
                                <p>客户群指标微分</p>
                            </li>
                        </ul>
                    </td>
                </tr>
            </table>
        </dd>
        
        <dd>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="noc_l"><span class="cj_down"></span><p>场景视频下载</p></td>
                    <td>
                        <ul class="help_list">
                        	<li onclick="to_down('bqcjkhq.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="标签创建客户群(培训材料场景一)">标签创建客户群</p>
                            </li>
                            <li onclick="to_down('bqgjsz.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p  title="标签告警设置(培训材料场景六)">标签告警设置</p>
                            </li>
                            <%
                   				if("true".equals(productMenu)){
                			%>
                            <li onclick="to_down('cpbqcjkhq.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p  title="产品标签创建客户群(培训材料场景三)">产品标签创建客户群</p>
                            </li>
                            <%
                   				}
                            %>
                            <li onclick="to_down('clydcj1.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="存量异动用户推荐换机场景1-标签创建客户群">存量异动用户推荐换机场景1-标签创建客户群</p>
                            </li>
                            <li onclick="to_down('clydcj2.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="存量异动用户推荐换机场景2客户群指标微分">存量异动用户推荐换机场景2客户群指标微分</p>
                            </li>
                            <li onclick="to_down('drkhq.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="导入客户群(培训材料场景二)">导入客户群</p>
                            </li>
                            <%
                				if("true".equals(marketingMenu)){
                			%>
                            <li onclick="to_down('khqtszyxglpt.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="客户群推送至营销管理平台(培训材料场景一)">客户群推送至营销管理平台</p>
                            </li>
                            <%
                				}
                            	if("true".equals(productMenu)){
                            %>
                            <li onclick="to_down('xskhcj.wmv');">
                            	<div class="cj_down_lit">&nbsp;</div>
                                <p title="学生客户场景-客户群匹配产品 营销策略匹配">学生客户场景-客户群匹配产品 营销策略匹配</p>
                            </li>
                            <%
                            	}
                            %>
                        </ul>
                    </td>
                </tr>
            </table>
        </dd>
        
        <dd>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="noc_l"><span class="ct_down"></span><p>内容文档下载</p></td>
                    <td>
                        <ul class="help_list">
                        	<li onclick="to_down('COCyhsc.pdf');">
                            	<div class="ct_down_lit">&nbsp;</div>
                                <p>用户手册</p>
                            </li>
                            <li onclick="to_down('COCsjzdv1.xlsx');">
                            	<div class="ct_down_lit">&nbsp;</div>
                                <p>数据字典</p>
                            </li>
                            <li onclick="to_down('khyyzxpxcl.pdf');">
                            	<div class="ct_down_lit">&nbsp;</div>
                                <p>培训材料</p>
                            </li>
                        </ul>
                    </td>
                </tr>
            </table>
        </dd>
        
        <dd>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="noc_l"><span class="player_down"></span><p>播放器下载</p></td>
                    <td>
                        <ul class="help_list">
                        	<li onclick="to_down('install_flash_player_ax.zip');">
                            	<div class="player_down_lit">&nbsp;</div>
                                <p>Flash播放器</p>
                            </li>
                        </ul>
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
</div>

</body>
</html>
