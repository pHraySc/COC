<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="date-setting-box">
    <dl>
		<dt>
			<div class="methodSel methodSelCurrent" value="1">日期范围</div>
		</dt>
		<dd>
			<div>
				<input type="text" id="startTime" class="fleft dateInputFmt80" readonly="readonly" 
						onclick="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}" 
						value="${pojo.startTime}"/>
				<span class="fleft padding15">至</span>
				<input type="text" name="pojo.endTime" id="endTime" readonly="readonly"  
                	class="fleft dateInputFmt80"  value="${pojo.endTime}"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
						/>
            </div>
        </dd>
       </dl>
	<!-- <dl>
		<dt>
			&nbsp;
		</dt>
		<dd>
			<p class="fleft rPadding88">
				 <input type="checkbox" id="dateLeftClosed" class="fleft" value="1"/>
                    <label for="dateLeftClosed" class="fleft"> 左闭区间</label>
			</p>
			<p class="fleft">
				<input type="checkbox" class="fleft" id="dateRightClosed" value="1"/>
                   <label for="dateRightClosed" class="fleft"> 右闭区间</label>
              </p>
           </dd>
       </dl> 
       <dl>
		<dt>
			&nbsp;
		</dt>
		<dd>
            <p class="fleft">
				<input type="checkbox" class="fleft" id="dynamicUpdate" value="1"/>
               <label for="dynamicUpdate" class="fleft"> 动态偏移更新</label>
             </p>
         </dd>
       </dl>-->
       <!-- 
       <dl>
		<dt>
			<div class="methodSel"  value="2">数值范围</div>
		</dt>
		<dd>
			<input type="text" value="" maxlength="4" id="exactValueDateYear"  name="exactValueDateYear" class="fleft inputFmt60"                     />
			<span class="fleft padding10">年</span>
			<input type="text" value="" maxlength="2" id="exactValueDateMonth" name="exactValueDateMonth" class="fleft inputFmt60"                     />
			<span class="fleft padding10">月</span>
			<input type="text" value="" maxlength="2" id="exactValueDateDay" name="exactValueDateDay" class="fleft inputFmt60"                     />
			<span class="fleft padding10">日</span>
		</dd>
       </dl>
        -->
       <dl>
		<dd>
			<!--<p class="cred">错误信息显示</p>
			--><div id="snameTip" class="tishi error" style="display:none;"></div>
		</dd>
       </dl>
       </div>
    <!-- 按钮操作-->
    <div class="btnActiveBox">
        <!-- <input type="button" value="确定" class="ensureBtn" onclick="filterSetting.date_submitSetDate();"/> -->
        <button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button>
   		<button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button>
    </div>