window.webtrendsAsyncInit=function(){ 
	var dcs=new Webtrends.dcs().init({ 
		dcsid:"dcs0kn42a10000kretw4itsl2_8m2d",
		domain:"20.26.17.128", 
		timezone:8, 
		i18n:true, 
		//fpcdom:".10086.cn", 
		fpc:"WT_FPC",
		metanames: "",
		paidsearchparams: "gclid,ad_id",
		adimpressions:true,
		adsparam:"WT.ac",
		dcsdelay: 2000,
		plugins:{ 
		} 
	});
	
	try{
		if(document.getElementById("custAccountForWT")){dcs.WT.mobile = document.getElementById("custAccountForWT").value;}
	}catch(te){};

	dcs.addSelector("input[type='button']", {
		transform: function(dcsObject, multiTrackObject) {
			var e = multiTrackObject.element || {};
			var t = e.id||e.name||"Unknown";
			multiTrackObject.argsa.push("WT.event","ButtonClick","WT.action",t);
		},
		delayTime: 50
	});

	dcs.track();
};

(function(){ 
var s=document.createElement("script"); s.async=true; s.src="webtrends.min.js"; 
var s2=document.getElementsByTagName("script")[0]; s2.parentNode.insertBefore(s,s2); 
}()); 


