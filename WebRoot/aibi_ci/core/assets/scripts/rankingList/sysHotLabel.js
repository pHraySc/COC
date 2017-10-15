/********
 *  2015-1-1
 *	鐑棬鏍囩
 */
SysHotLabel = function() {
	var SysHotLabel = {
		/**
		 * 鎵�湁鏌ヨ鍒楄〃鍏ュ彛
		 */
		show	: function(target) {
	        var actionUrl = $.ctx + '/core/ciRankingListAction!findSysHotRankLabel.ai2do?dataScope='+target.attr("datescope");
	        $.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //娣诲姞閬僵灞�
	        $.ajax({
	            url		: actionUrl,
	            type	: 'POST',
	            dataType: 'html',
	            success	:function(html){
	                $('#sysHotLabelRankingList').html('');
	                $('#sysHotLabelRankingList').html(html);
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
			 var actionUrl = $.ctx + '/core/ciRankingListAction!findSysHotRankLabel.ai2do?dataScope=oneWeek';
			 $.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //娣诲姞閬僵灞�
		        $.ajax({
		            url		: actionUrl,
		            type	: 'POST',
		            dataType: 'html',
		            success	:function(html){
		                $('#sysHotLabelRankingList').html('');
		                $('#sysHotLabelRankingList').html(html);
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
		addLabelShopCart : function(target) {
			var labelId = $(target).attr("labelId");
			coreMain.addShopCart(labelId, 1, 0, "");
			return true;
		}
	}
	return SysHotLabel;
}();