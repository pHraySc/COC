/********
 *  2015-1-1
 *	鐑棬鏍囩
 */
SysHotCustom = function() {
	var SysHotCustom = {
		/**
		 * 鎵�湁鏌ヨ鍒楄〃鍏ュ彛
		 */
		show	: function(target) {
	        var actionUrl = $.ctx + '/core/ciRankingListAction!sysHotCustomRankingList.ai2do?ciCustomGroupInfo.dateType='+target.attr("datescope");
	        $.blockUI({message: "<h1 style='margin: 5px;'>鍒楄〃鍔犺浇涓�..</h1>"}); //娣诲姞閬僵灞�
	        $.ajax({
	            url		: actionUrl,
	            type	: 'POST',
	            dataType: 'html',
	            success	:function(html){
	                $('#sysHotCustomRankingList').html('');
	                $('#sysHotCustomRankingList').html(html);
	            },
			   	complete: function() {
			   		$.unblockUI(); 
			   	},
			   	stop: function() {
			   		$.unblockUI(); 
			   	}
	        });
	    },
	    /**
	     * 鏄剧ず鍒楄〃
	     */
		init	: function() {
			var actionUrl = $.ctx + '/core/ciRankingListAction!sysHotCustomRankingList.ai2do?ciCustomGroupInfo.dateType=4';
			$.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //娣诲姞閬僵灞�
	        $.ajax({
	            url		: actionUrl,
	            type	: 'POST',
	            dataType: 'html',
	            success	:function(html){
	                $('#sysHotCustomRankingList').html('');
	                $('#sysHotCustomRankingList').html(html);
	            },
			   	complete: function() {
			   		$.unblockUI(); 
			   	},
			   	stop: function() {
			   		$.unblockUI(); 
			   	}
	        });
	    },
		/**
		 * 鍔犲叆鍒拌喘鐗╄溅
		 * @parameter target 瑙﹀彂鏃堕棿鑺傜偣
		 */
		addCustomShopCart : function(target) {
			var customId = $(target).attr("customGroupId");
			coreMain.addShopCart(customId, 2, 0, "");
			return true;
		}
	}
	return SysHotCustom;
}();