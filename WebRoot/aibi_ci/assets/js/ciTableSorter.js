function sortTable() {
	var dataType = $(this).attr('dataType');
	if(dataType!=undefined){
		var index = $('#sortTable th').index(this) + 1;
		var arr = [];
		var order = "asc";
		var row = $('#sortTable tbody tr');
		var noSort = [];
		var j = 0;
		var k = 0;
		$.each(row, function(i) {
			if($(row[i]).attr("isSort") != null && $(row[i]).attr("isSort") == "noSort" ){
				noSort[j] = row[i];
				j ++;
			}else{
				arr[k] = row[i];
				k ++;
			}
		});
		if($(this).hasClass('headerSortDown')){
			arr.sort(sortStr(index, dataType,"asc"));
			$(this).removeClass("headerSortDown").addClass(" headerSortUp");
		}else if($(this).hasClass('headerSortUp')){
			arr.sort(sortStr(index, dataType,"desc"));
            $(this).removeClass("headerSortUp").addClass("headerSortDown");	
        }else{
        	arr.sort(sortStr(index, dataType,"asc"));
			$(this).removeClass("headerSortDown").addClass(" headerSortUp");
        }
		var fragment = document.createDocumentFragment();
		$.each(arr, function(i) {
			if(i%2 == 0){
				if($(arr[i]).hasClass("odd")){
					$(arr[i]).removeClass("odd").addClass("even");
				}
			}else{
				if($(arr[i]).hasClass("even")){
					$(arr[i]).removeClass("even").addClass("odd");
				}
			}
			fragment.appendChild(arr[i]);
		});
		$.each(noSort, function(i) {
			if((i+arr.length)%2 == 0){
				if($(noSort[i]).hasClass("odd")){
					$(noSort[i]).removeClass("odd").addClass("even");
				}
			}else{
				if($(noSort[i]).hasClass("even")){
					$(noSort[i]).removeClass("even").addClass("odd");
				}
			}
			fragment.appendChild(noSort[i]);
		});
		$('#sortTable tbody').append(fragment);
	}
}
function sortStr(index, dataType,order) {
	return function(a, b) {
		var aText = $(a).find('td:nth-child(' + index + ')').text();
		var bText = $(b).find('td:nth-child(' + index + ')').text();

		if (dataType != 'text') {
			aText = Parse(aText, dataType)
			bText = Parse(bText, dataType)
			if(order=="desc"){
				return aText > bText ? -1 : bText > aText ? 1 : 0;
			}else{
				return aText>bText? 1:bText>aText? -1:0;
			}
		} else {
			if(order == "desc")
				return bText.localeCompare(aText);
			else
				return aText.localeCompare(bText)
		}
	}
}
function Parse(data, dataType) {
	switch (dataType) {
	case 'num':
		data = formateData(data, dataType);
		return parseFloat(data) || 0;
	case 'date':
		data = formateData(data, dataType);
		return parseFloat(data) || 0;
	default:
		return splitStr(data);
	}
}
function formateData(data, dataType){
	data = data.Trim();
	if(dataType == "num"){
		if(data == "-"){
			data = "-1";
		}else{
			while(data.indexOf(",")>=0){
				data = data.replace(",","");
			}
		}
	}else if(dataType == "date"){
		if(data == "-"){
			data = "0";
		}else{
			while(data.indexOf("-")>=0){
				data = data.replace("-","");
			}
			while(data.indexOf(":")>=0){
				data = data.replace(":","");
			}
			data = data.replace(" ","");
		}
	}
	return data;
}
function splitStr(data) {
	var re = /^[\$a-zA-z\u4e00-\u9fa5 ]*(.*?)[a-zA-z\u4e00-\u9fa5 ]*$/;
	data = data.replace(re, '$1');
	return parseFloat(data);
}

