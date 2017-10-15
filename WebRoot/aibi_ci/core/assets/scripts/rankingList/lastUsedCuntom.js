/********
 *  2015-1-1
 *	鏈�柊瀹㈡埛缇�
 */
LastUsedCuntom = function() {
	var LastUsedCuntom = {
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
	return LastUsedCuntom;
}();