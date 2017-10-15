<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-cia-frame">
  <div class="center">
    	<div>
        	<ul class="">
            	<li>属性名称： </li>
                <li><input id="customAttrName" type="text" name="textfield" id="textfield" value="输入名称，限N字" class="input-box" onFocus="if(value==defaultValue){value='';this.style.color='#333'}"/></li>
            </ul>
        </div>
        <div class="bg">
        	<ul style="height:30px; line-height:30px;">
            	<li class="fleft">计算表达式</li>
                <li><i class="cia-icon table-icon-jia fleft cursor"></i></li>
                <li><button type="button" class="cia-show-button_row cursor ">用户用网日期</button></li>
                <li>
                	 <input type=text id="customSystemAttr" name="customSystemAttr" defaultValue="" originalValue="">
	                 <input id="customSystemAttrHidden" name="customSystemAttrHidden" type="hidden" defaultValue="" originalValue=""/>
                </li>
        </ul>
            <ul>
            	<li><button id="clearBut" type="button" class="cia-show-button_row cursor fright">重置</button></li>
            </ul>
        </div>
        <div class="operation">
        	<span class="operation-left fleft">
            	<ul>
                	<li class="operation-title">操作符</li>
                	<li><i id="icon-jiahao" class="cia-icon table-icon-jiahao"></i></li>
                    <li><i id="icon-jianhao" class="cia-icon table-icon-jianhao"></i></li>
                    <li><i id="icon-cheng" class="cia-icon table-icon-cheng"></i></li>
                    <li><i id="icon-chufa" class="cia-icon table-icon-chufa"></i></li>
                    <li><i id="icon-zuo" class="cia-icon table-icon-zuo"></i></li>
                    <li><i id="icon-you" class="cia-icon table-icon-you"></i></li>
                    <li><input id="constantVal" name="constantVal" type="text"></li>
                </ul>
            </span>
            <span class="operation-right fleft">
            	<ul>
                	<li class="operation-title">属性选择</li>
                </ul>
                <ul class="operation-right-li">
                </ul>
            </span>
        </div>
	</div>
	<div class="bottom cb">
    	<ul>
        	<li><button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button></li>
         	<li><button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button></li>
        </ul>
  </div>
</div>