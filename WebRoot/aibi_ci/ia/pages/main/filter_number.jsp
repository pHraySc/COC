 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <div class="number-value-setting-box">

	        <dl>
	            <dt>
	            <div class="queryMethod"  value="1">数值范围</div>
	            </dt>
	            <dd>
	                  <ol class="fleft numberBoxUl">
	                    <li>
	                         <input type="text" class="fleft inputFmt100" id="contiueMinVal"  value="输入下限" onkeyup="$('#cnameTip').hide();"/>
	                         <span class="fleft inputSegSpace">—</span>
	                         <input type="text" class="fleft inputFmt100" id="contiueMaxVal" value="输入上限" onkeyup="$('#cnameTip').hide();"/>
	                         <span class="fleft unitColor" id="unit1520"></span>
	                         <div id="cnameTip" class="tishi error" style="display:none;"></div>
	                    </li>
	                    <!-- <li>
	                            <p class="fleft sectionBox">
	                                <input type="checkbox"  id="leftClosed" value="1" class="fleft" />
	                                <label for="leftClosed" class="fleft"> 左闭区间</label>
	                            </p>

	                            <p class="fleft sectionBox">
	                                <input type="checkbox"   class="fleft" id="rightClosed"  value="1" />
	                                <label for="rightClosed" class="fleft"> 右闭区间</label>
	                            </p>
	                    </li> -->
	                  </ol>
	            </dd>
	        </dl>
	        <dl class="moddel">
	            <dt>
	            <div class="queryMethod"  value="2">精确查询</div>
	            </dt>
	            <dd>
					<div >
					   <input type="text" class="exactQueryInput fleft"  id="exactValue" value="" onkeyup="$('#snameTip').hide();"/>
					   <span class="fleft unitColor" id="unit1520"></span>
					   <div id="snameTip" class="tishi error" style="display:none;"></div>
					</div>
                    <div class="fleft useTip">
                                                     多条件用","分割且不能用","结尾
                    </div>
	            </dd>
	        </dl>
	       </div>
	    <!-- 按钮操作-->
	    <div class="btnActiveBox">
	        <!-- <input type="button" value="确定" class="ensureBtn"  id="numberValueSetEnsureBtn" onclick="setNumberValue();"/> -->
	        <button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button>
   			<button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button>
	    </div>