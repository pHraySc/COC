$(function(){	
	//选择框
	$( '.tag_box_manage #select_box' ).click(function( e ){
		e.preventDefault();
		var div = $( this ).parents( '.tag_box_manage' ).siblings( '.form-field' ).show();
		var dom = div.get( 0 );
		dom.out = setTimeout(function(){
			div.hide();
		},6000);
	}).parents( '.tag_box_manage' ).siblings( '.form-field' ).bind( 'mouseleave', function( e ){
		clearTimeout( this.out );
		this.out = setTimeout(function(){
			$( e.currentTarget ).hide();
		},200);
	}).bind( 'mouseenter',function( e ){
		clearTimeout( this.out );
	});
	$('.form-field li a').click(function( e ){
		var a = $( this );
		a.parents( 'ul' ).find( 'li.on' ).removeClass( 'on' );
		a.parent( 'li' ).addClass( 'on' );
		$( '.tag_box_manage #select_box .like_button_middle:first span' ).html(a.html());
		a.parents('.form-field').hide();
	});

})
