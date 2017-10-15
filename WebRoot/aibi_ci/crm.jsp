<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.ailk.biapp.ci.model.CrmTypeModel" %>
<%@ page import="com.ailk.biapp.ci.model.CrmLabelOrIndexModel" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="com.ailk.biapp.ci.dataservice.service.ICiLabelsAndIndexesService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标签信息</title>
<link href="<%=request.getContextPath()%>/aibi_ci/assets/themes/common.css" rel="stylesheet"  />
<link href="<%=request.getContextPath()%>/aibi_ci/assets/themes/default/main.css" rel="stylesheet"  />
<script type="text/javascript" src="<%=request.getContextPath()%>/aibi_ci/assets/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/aibi_ci/assets/js/main.js"></script>
<script type="text/javascript">
$(function(){
    resize_crmMain();
    $(window).resize(resize_crmMain);

    $("#crm_main ul.ct li").mouseenter(function(){
        $(this).addClass("onhover");
        var tar=$(this).find(".slide");
        tar.height()<75?tar.height(75):"";
        var _h=tar.innerHeight();
        $(this).find(".title").height(_h).find("p").height(_h-57);

    }).mouseleave(function(){
                $(this).removeClass("onhover");
                var tar=$(this).find(".slide");
                tar.height()>75?tar.height(75):"";
                $(this).find(".title").height(80);
            })

})

function resize_crmMain(){
    var docWidth=$(document).width()-10;
    $("#crm_main").width(docWidth);
    $("#crm_main ul.ct li").each(function(){
        var li_width=(docWidth-5)/2;
        $(this).width(li_width);
        $(this).find(".right").width(li_width-93);
        $(this).find(".right .slide").width(li_width-95);
    });
}
</script>
</head>
<body>
<%
    String sysCode = (String)request.getParameter("sysCode");
	String channelId = (String)request.getParameter("channelId");
	String accNbr = (String)request.getParameter("accNbr");
	String staffCode = (String)request.getParameter("staffCode");
	String acpId = (String)request.getParameter("acpId");
    ApplicationContext context = WebApplicationContextUtils.
            getWebApplicationContext(request.getSession().getServletContext());
    ICiLabelsAndIndexesService service  = (ICiLabelsAndIndexesService)context.getBean("LabelsAndIndexesService");
    List<CrmTypeModel> crmTypeModelList= service.getCrmTypeModelByMobile(sysCode,channelId,accNbr,staffCode,acpId);
    String[] typeStyle = new String[]{"title title01","title title04","title title02","title title05","title title03","title title06"};
%>
<div id="crm_main" class="crm_main">
	<ul class="ct">
        <%
        for(int typeIdx = 0;typeIdx<crmTypeModelList.size();typeIdx++){
            CrmTypeModel crmTypeModel = crmTypeModelList.get(typeIdx);
            String seprateStyle = "";
            if(typeIdx % 2 ==1){
                seprateStyle = "class=\"fright\"";
            }
            out.print("<li "+seprateStyle+"><div class=\""+typeStyle[typeIdx]+"\"><p>"+crmTypeModel.getTypeName()+"</p></div>");
            out.print("<div class=\"right\"><div class=\"slide\">");
            for(CrmLabelOrIndexModel model :crmTypeModel.getDisplayLabelOrIndexList()){
                String style = "";
                if(model.isHighLight()){
                    if(typeIdx%2==0){
                        style = "class=\"star_green bold\"";
                    } else{
                        style = "class=\"star_blue bold\"";
                    }
                }
                out.print("<span "+style+">"+model.getDisplayName()+"&nbsp</span>");
            }
            out.print("</div></div></li>");
        }
        %>
    </ul>
    <div class="ft">
    	当前用户为：<font><%=accNbr %></font>
    </div>
</div>
</body>
</html>
