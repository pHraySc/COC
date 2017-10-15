(function($){
	$.fn.tzCheckbox = function(options){
		
		// Default On / Off labels:
		
		options = $.extend({
			labels : ['ON','OFF']
		},options);
		
		return this.each(function(){
			var originalCheckBox = $(this),
				labels = [];

			// Checking for the data-on / data-off HTML5 data attributes:
			if(originalCheckBox.data('on')){
				labels[0] = originalCheckBox.data('on');
				labels[1] = originalCheckBox.data('off');
			}
			else labels = options.labels;

			// Creating the new checkbox markup:
			
			var checkof=(this.checked?'checked':'')
			var checkBox = $("<span class='tzCheckBox "+checkof+"'><span class='tzCBPart'></span></span>");

			// Inserting the new checkbox, and hiding the original:
			checkBox.insertAfter(originalCheckBox.hide());

			checkBox.click(function(){
				checkBox.toggleClass('checked');
				//alert("hello");
				
				var isChecked = checkBox.hasClass('checked');
				
				// Synchronizing the original checkbox:
				originalCheckBox.attr('checked',isChecked);
				checkBox.find('.tzCBContent').html(labels[isChecked?0:1]);
				
				if(options.clickFn){
                    options.clickFn(originalCheckBox);
                }
				//userNoticeSet(originalCheckBox,isChecked);
			});
			
			// Listening for changes on the original and affecting the new one:
			originalCheckBox.bind('change',function(){
				checkBox.click();
			});
		});
	};
})(jQuery);
$(document).ready(function(){
	
	
});