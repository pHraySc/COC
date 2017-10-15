/********
 *  2015-1-1
 *	鏈�柊鏍囩
 */
LastPublishRankLabel = function() {
	var LastPublishRankLabel = {
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
	return LastPublishRankLabel;
}();