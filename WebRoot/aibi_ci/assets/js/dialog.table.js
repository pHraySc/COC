
function changeTableColor(obj){
	$(obj + " tr:visible").each(function(i){
		$(this).removeClass('even').removeClass("odd");
		if(i%2 == 0){
			$(this).addClass('even');
		}else{
			$(this).addClass("odd");
		}
	});
}
