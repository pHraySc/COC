jQuery.extend({ 
	isNotBlank: function( val ) {
		if(val && typeof val != "undefined" && $.trim(val).length != 0){
			return true;
		}else{
			return false;
		}
	
	}
		
});