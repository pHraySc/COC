<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-cia-frame">
  <div class="center"> 
        	<ul class="fleft divw">
            	<li class="fleft">函数:</li> 
             <li class="fleft even">
             	<label>
	             	<input type="radio" name="aggregation" id="radio1" value="1" />求合
             	</label>
             </li>
             <li class="fleft ">
             	<label>
             		<input type="radio" name="aggregation" id="radio2" value="2" />平均
             	</label>
             </li>
             <li class="fleft even">
          	    <label>
             		<input type="radio" name="aggregation" id="radio3" value="3" />计数
             	</label>
             </li>
             <li class="fleft" >
             	<label>
             		<input type="radio" name="aggregation" id="radio4" value="4" />最大值
             	</label>
             </li>
             <li class="fleft even">
           		<label>
             		<input type="radio" name="aggregation" id="radio5" value="5" />最小值
             	</label>
             </li>
         </ul> 
	</div>
	<div class="bottom">
    	<ul>
        	<li><button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button></li>
         	<li><button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button></li>
        </ul>
  </div>
</div>
