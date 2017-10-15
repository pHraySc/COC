<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-cia-frame" >
  <div class="center">
  	<ul class=" clearfix divw" >
      	  <li class="left">
 	  		<label>
      	  		<input type="radio" name="isIsometry" id="radio1" value="1" style="margin-top:10px;" />等距分档
     	  	</label>
    	  	  </li>
          <li class="left">
          		<span class="margin-lr-5">指定分档组数：</span>
          		<select id="gradeNum">
	          		<option value="">请选择</option>
	            		<option value="5">5</option>
	                	<option value="4">4</option>
					<option value="3">3</option>
					<option value="2">2</option>
					<option value="1">1</option>
				</select>
			</li> 
            	<li id="isIsometryLi" class="fleft">
            		<div class="isIsometry-div">
            			<label>
	            			<input type="radio" name="isIsometry" id="radio0" value="0" style="margin-top:10px;" />不等距分档 
	            		</label>
            		</div>
            		<div class="isIsometry-div J_isIsometry">
            			<p class="gradeName-box fleft first">
		                	<input type="text" name="gradeName" id="textfield" class="gradeName"/>
            			</p>
            			<p class="gradeName-box fleft">
		                  <input type="text" name="gradeCustom" id="textfield" class="gradeName"/>
            			</p> 
	                
	                	<p class="gradeName-link fleft">
            				<span>——</span>
            			</p> 
            			<p class="gradeName-box fleft">
	                    	<input type="text" name="gradeCustom" id="textfield" class="gradeName"/>
            			</p> 
		                <i id="addNewGrade" class="table-icon-off fleft cursor"></i>
		                <i id="delGrade" class="table-icon-on fleft cursor" ></i>
            		</div>
            <li>
        </ul>
	</div>
	<div class="bottom">
	    	<ul>
	        	<li><button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button></li>
         	<li><button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button></li>
	    </ul>
  </div>
</div>
