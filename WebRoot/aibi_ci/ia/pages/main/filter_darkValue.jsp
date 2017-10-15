<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="dim-match-box clearfix">
		<div class="fleft dim-match-left-box minWidth"></div>
		<dl>
			<dt>
				<div class="dimMatchMethod current" value="1">模糊值</div>
			</dt>
			<dd class="dim-match-dd">
				<div class="fright dim-match-right-box">
					<!--<h6> 请输入模糊匹配内容：</h6>
					--><input type="text"  class="dimMatchInput"   id="darkValue" onkeyup="$('#snameTip').hide();"/>
					<div id="snameTip" class="tishi error" style="display:none;"></div> 
				</div>
			</dd>
		</dl>
		<dl>
			<dt>
				<div class="dimMatchMethod" value="2">精确值</div>
			</dt>
			<dd  class="dim-match-dd">
				<div class="fright dim-match-right-box">
					<input type="text"  class="dimMatchInput" id="exactValue" onkeyup="$('#enameTip').hide();$('#newTip').hide();" onkeydown="if(event.keyCode==32) return false"/>
					<!-- <div class="itemChooseTopBox uploadify_div" id="uploadify_div">
		       			<input type="file" name="file_upload_dark" id="file_upload_dark" />
		  		 	</div> -->
					<div id="enameTip" class="tishi error" style="display:none;"></div><!-- 非空验证提示 -->
				</div>
				<div class="fright newTip newTip-dim-match" id="newTip"></div>
			</dd>
		</dl>
    </div>
	<div class="importFontStyleBox" id="importFontStyleBox"></div>
    <!-- 按钮操作-->
    <div class="btnActiveBox">
		<!-- <input type="button" value="确定" class="ensureBtn" id="darkValueSetEnsureBtn" onclick="setDarkValue();"/> -->
		<button type="button" class="cia-show-button cursor fright" id="but_ensure">确定</button>
   		<button type="button" class="cia-show-button cursor fright no" id="but_cancel">取消</button>
    </div>