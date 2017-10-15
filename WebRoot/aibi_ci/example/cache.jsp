<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.ailk.biapp.ci.constant.CommonConstants"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
</head>
<body>
<div>
  <table>
    <tr>
      <td>从java中取name的方法：</td>
      <td>CacheBase.getInstance().getNameByKey(CommonConstants.TABLE_DIM_LABEL_USE_TYPE,new Integer(1));</td>
      <td>--------${name }</td>
    </tr>
    <tr>
      <td>用标签取name的方法：</td>
      <td>&lt;base:idToName id="1" tableName="&lt;%=CommonConstants.TABLE_DIM_LABEL_USE_TYPE %&gt;"/&gt;</td>
      <td>--------<base:idToName id="1" tableName="<%=CommonConstants.TABLE_DIM_LABEL_USE_TYPE %>"/></td>
    </tr>
    <tr>
      <td>下拉选择框-----</td>
      <td>
                标签使用类型<base:DCselect selectName="labelUseType" tableName="<%=CommonConstants.TABLE_DIM_LABEL_USE_TYPE%>" 
                nullName="--请选择--" nullValue="" selectValue="1"/> 
      </td>
    </tr>
    
    <tr>
      <td>下拉选择框-----</td>
      <td>
                标签数据状态<base:DCselect selectName="lableDataStatus" tableName="<%=CommonConstants.TABLE_DIM_LABEL_DATA_STATUS%>" 
                nullName="--请选择--" nullValue="" selectValue="1"/> 
      </td>
    </tr>
  </table>
  
</div>

<!-- area_data start -->
</body>
</html>