
function aibiTableChangeColor(obj){
	$(obj+" tr:even").addClass("even");
	$(obj+" tr:odd").addClass("odd");
	
	if(!$(obj).hasClass("nohover")){
		$(obj+ " tr").hover(function(){
			$(this).addClass('hover');
		},function(){
			$(this).removeClass('hover');
		});
	}
	if(!$(obj).hasClass("noselect_tr")){
		$(obj+" tr").click(function(){
			if($(this).hasClass('aibi_tr_selected')){
				
			}
			else{
				$(obj+ " tr").removeClass('aibi_tr_selected');	
				$(this).addClass('aibi_tr_selected');
			}
		})
	}
}
