<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-cia-frame">
  <div class="center">
        <div class="pattern-div clearfix clearfix">
	        	<ul class=" pattern-ul clearfix">
	            	<li class="fleft">日期：</li> 
	             <li class="fleft">
	             	<label>
		             	<input type="radio" name="pattern" id="radio1" value="1" />YYYY-MM-DD
	             	</label>
	             </li>
	             <li class="fleft">
	             	<label>
		             	<input type="radio" name="pattern" id="radio2" value="2" />YYYYMMDD
	             	</label>
	             </li>
	             <li class="fleft">
	             	<label>
		             	<input type="radio" name="pattern" id="radio3" value="3" />YYYY-MM
	             	</label>
	             </li>
	             <li class="fleft">
	             	<label>
		             	<input type="radio" name="pattern" id="radio4" value="4" />YYYYMM
	             	</label>
	             </li>
	             <li class="fleft">
	             	<label>
		             	<input type="radio" name="pattern" id="radio0" value="0" />保留原格式
	             	</label>
	             </li>
            </ul>
        </div>
        <div class="pattern-div clearfix even">
	        	<ul class="pattern-ul clearfix">
	            	<li class="fleft">分位：</li> 
                 <li class="fleft">
                 	<label>
                 		<input type="radio" name="title" id="radio5" value="5" />千分位
                 	</label>
                 </li>
                 <li class="fleft">
                 	<label>
                 		<input type="radio" name="title" id="radio6" value="6" />百分位
                 	</label>
                 </li>
       		 </ul>
        </div>
        <div class="pattern-div clearfix">
        		<ul class="pattern-ul clearfix">
           	 	<li class="fleft">货币：</li> 
                <li class="fleft"><input type="radio" name="title" id="radio7" value="7" />￥</li>
            </ul>
        </div>
         <div class="pattern-div clearfix even">
	        	<ul class="pattern-ul clearfix ">
	            	<li class="fleft">单位：</li> 
	             <li class="fleft">
	        			<select class="select" id="unit">
						<option value="">请选择</option>
						<option value="元">元</option>
						<option value="M">M</option>
						<option value="9">自定义</option>
					</select>
		             <span class="divh"><input type="text" name="textfield" id="customUnit" /></span>
	             </li>
	         </ul>
         </div>
	</div>
	<div class="bottom">
   		 <button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button>
   		 <button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button>
	</div>
</div>

